package com.g4mesoft.minecraft.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.MinecraftApp;
import com.g4mesoft.minecraft.world.block.Block;
import com.g4mesoft.minecraft.world.block.MutableBlockPosition;
import com.g4mesoft.minecraft.world.block.PlantBlock;
import com.g4mesoft.minecraft.world.block.PlantType;
import com.g4mesoft.minecraft.world.block.IBlockPosition;
import com.g4mesoft.minecraft.world.block.ImmutableBlockPosition;
import com.g4mesoft.minecraft.world.block.state.BlockState;
import com.g4mesoft.minecraft.world.entity.PlayerEntity;
import com.g4mesoft.minecraft.world.gen.DiamondNoise;
import com.g4mesoft.world.phys.AABB3;

public class World {
	
	public static final int WORLD_HEIGHT = WorldChunk.CHUNK_SIZE * 5;
	
	public static final int CHUNKS_X = 8;
	public static final int CHUNKS_Z = 8;
	
	private static final int RANDOM_TICK_SPEED = 3;
	
	private final MinecraftApp app;
	
	private final WorldChunk[] chunks;
	private final Random random;
	
	private BlockRay blockRay;
	private PlayerEntity player;
	
	public World(MinecraftApp app) {
		this.app = app;

		chunks = new WorldChunk[CHUNKS_X * CHUNKS_Z];
		random = new Random();

		blockRay = new BlockRay(this, 0.01f);
		player = new PlayerEntity(this);
	}
	
	public void generateWorld() {
		DiamondNoise noise = new DiamondNoise(CHUNKS_X * WorldChunk.CHUNK_SIZE, random);
		
		int i = 0;
		for (int cz = 0; cz < CHUNKS_Z; cz++) {
			for (int cx = 0; cx < CHUNKS_X; cx++) {
				WorldChunk chunk = new WorldChunk(cx, cz);
				chunk.generateChunk(noise, random);
				chunks[i++] = chunk;
			}
		}
		
		for (i = 0; i < CHUNKS_Z * CHUNKS_X; i++)
			populateChunk(chunks[i]);
	}
	
	private void populateChunk(WorldChunk chunk) {
		MutableBlockPosition blockPos = new MutableBlockPosition();
		
		int x0 = chunk.getChunkX() * WorldChunk.CHUNK_SIZE;
		int z0 = chunk.getChunkZ() * WorldChunk.CHUNK_SIZE;
		int x1 = x0 + WorldChunk.CHUNK_SIZE;
		int z1 = z0 + WorldChunk.CHUNK_SIZE;
		
		for (blockPos.z = z0; blockPos.z < z1; blockPos.z++) {
			for (blockPos.x = x0; blockPos.x < x1; blockPos.x++) {
				blockPos.y = chunk.getHighestPoint(blockPos) + 1;
				
				if (random.nextInt(160) == 0) {
					growTree(blockPos);

					blockPos.y--;
					
					setBlock(blockPos, Blocks.DIRT_BLOCK);
				} else if (random.nextInt(20) == 0) {
					BlockState plantState = Blocks.PLANT_BLOCK.getDefaultState();
					
					PlantType type = PlantType.PLANT_TYPES[random.nextInt(PlantType.PLANT_TYPES.length)];
					plantState = plantState.withProperty(PlantBlock.PLANT_TYPE_PROPERTY, type);
					
					setBlockState(blockPos, plantState);
				}
			}
		}
	}
	
	public void growTree(IBlockPosition blockPos) {
		int treeHeight = 5 + random.nextInt(3);
		int trunkHeight = Math.max(1, treeHeight - 5);
		
		MutableBlockPosition tmpPos = new MutableBlockPosition(blockPos);
		
		for (int i = 0; i < treeHeight; i++) {
			if (i > trunkHeight) {
				int yo = i - trunkHeight;
				
				for (int zo = -3; zo <= 3; zo++) {
					for (int xo = -3; xo <= 3; xo++) {
						tmpPos.x = blockPos.getX() + xo;
						tmpPos.z = blockPos.getZ() + zo;
						
						if (getBlock(tmpPos) == Blocks.AIR_BLOCK) {
							int distSqr = xo * xo + yo * yo + zo * zo;
							
							if (distSqr < 3 * 2 * 3)
								setBlock(tmpPos, Blocks.LEAVES_BLOCK);
						}
					}
				}
			}

			if (i < treeHeight - 1) {
				tmpPos.x = blockPos.getX();
				tmpPos.z = blockPos.getZ();
				
				setBlock(tmpPos, Blocks.WOOD_LOG_BLOCK);
			}

			tmpPos.y++;
		}
	}
	
	public BlockHitResult castBlockRay(float x, float y, float z, Vec3f dir) {
		return blockRay.castRay(x, y, z, dir);
	}
	
	public WorldChunk getChunk(IBlockPosition blockPos) {
		if (blockPos.getY() < 0 || blockPos.getY() >= WORLD_HEIGHT)
			return null;
		
		int chunkX = Math.floorDiv(blockPos.getX(), WorldChunk.CHUNK_SIZE);
		int chunkZ = Math.floorDiv(blockPos.getZ(), WorldChunk.CHUNK_SIZE);
		
		return getChunk(chunkX, chunkZ);
	}

	public WorldChunk getChunk(int chunkX, int chunkZ) {
		if (chunkX < 0 || chunkX >= CHUNKS_X)
			return null;
		if (chunkZ < 0 || chunkZ >= CHUNKS_Z)
			return null;
		
		return chunks[chunkX + chunkZ * CHUNKS_X];
	}
	
	public BlockState getBlockState(IBlockPosition blockPos) {
		WorldChunk chunk = getChunk(blockPos);
		if (chunk == null)
			return Blocks.AIR_BLOCK.getDefaultState();
		return chunk.getBlockState(blockPos);
	}
	
	public void setBlockState(IBlockPosition blockPos, BlockState state) {
		WorldChunk chunk = getChunk(blockPos);
		if (chunk != null) {
			BlockState oldState = chunk.getBlockState(blockPos);

			if (oldState != state) {
				int oldHighestPoint = chunk.getHighestPoint(blockPos);
				
				if (chunk.setBlockState(blockPos, state)) {
					int highestPoint = chunk.getHighestPoint(blockPos);
					
					if (oldHighestPoint != highestPoint) {
						int x = blockPos.getX();
						int z = blockPos.getZ();
						
						markRangeDirty(new ImmutableBlockPosition(x, oldHighestPoint, z), 
						               new ImmutableBlockPosition(x,    highestPoint, z), true);
					} else {
						Block oldBlock = oldState.getBlock();
						Block newBlock = state.getBlock();
						
						if (oldBlock.isSolid() || newBlock.isSolid()) {
							markDirty(blockPos, (oldBlock != newBlock));
						} else {
							markDirty(blockPos, false);
						}
					}
				}
			}
		}
	}

	public Block getBlock(IBlockPosition blockPos) {
		return getBlockState(blockPos).getBlock();
	}
	
	public void setBlock(IBlockPosition blockPos, Block block) {
		setBlockState(blockPos, block.getDefaultState());
	}
	
	public int getHighestPoint(IBlockPosition blockPos) {
		WorldChunk chunk = getChunk(blockPos);
		if (chunk == null)
			return 0;
		return chunk.getHighestPoint(blockPos);
	}
	
	private void markRangeDirty(IBlockPosition p0, IBlockPosition p1, boolean includeBorders) {
		app.getWorldRenderer().markRangeDirty(p0, p1, includeBorders);
	}

	private void markDirty(IBlockPosition blockPos, boolean includeBorders) {
		app.getWorldRenderer().markDirty(blockPos, includeBorders);
	}
	
	public boolean isLoadedBlock(IBlockPosition blockPos) {
		return getChunk(blockPos) != null;
	}

	public boolean isLoadedBlock(int chunkX, int chunkZ) {
		return getChunk(chunkX, chunkZ) != null;
	}
	
	public void update() {
		player.update();
		
		updateRandomTick();
	}
	
	private void updateRandomTick() {
		MutableBlockPosition pos = new MutableBlockPosition();

		for (int chunkX = 0; chunkX < CHUNKS_X; chunkX++) {
			for (int chunkZ = 0; chunkZ < CHUNKS_Z; chunkZ++) {
				WorldChunk chunk = getChunk(chunkX, chunkZ);
				if (chunk != null && chunk.isRandomTicked()) {
					for (int i = 0; i < RANDOM_TICK_SPEED; i++) {
						pos.x = random.nextInt(WorldChunk.CHUNK_SIZE) + chunkX * WorldChunk.CHUNK_SIZE;
						pos.y = random.nextInt(WORLD_HEIGHT);
						pos.z = random.nextInt(WorldChunk.CHUNK_SIZE) + chunkZ * WorldChunk.CHUNK_SIZE;

						BlockState state = chunk.getBlockState(pos);
						Block block = state.getBlock();
						
						if (block.isRandomTicked())
							block.randomTick(this, pos, state, random);
					}
				}
			}
		}
	}

	public int getHeight() {
		return WORLD_HEIGHT;
	}
	
	public PlayerEntity getPlayer() {
		return player;
	}

	public List<AABB3> getBlockHitboxes(AABB3 hitbox) {
		List<AABB3> hitboxes = new ArrayList<AABB3>();
		
		MutableBlockPosition pos = new MutableBlockPosition();
		
		int x1 = (int)hitbox.x1;
		int y1 = (int)hitbox.y1;
		int z1 = (int)hitbox.z1;
		
		for (pos.x = (int)hitbox.x0; pos.x <= x1; pos.x++) {
			for (pos.y = (int)hitbox.y0; pos.y <= y1; pos.y++) {
				for (pos.z = (int)hitbox.z0; pos.z <= z1; pos.z++) {
					BlockState state = getBlockState(pos);
					state.getBlock().getEntityHitboxes(this, pos, state, hitboxes);
				}
			}
		}
		
		return hitboxes;
	}
}
