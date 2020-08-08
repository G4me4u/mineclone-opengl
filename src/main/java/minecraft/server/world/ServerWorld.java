package minecraft.server.world;

import minecraft.client.MinecraftClient;
import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.World;
import minecraft.common.world.WorldChunk;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.ImmutableBlockPosition;
import minecraft.common.world.block.MutableBlockPosition;
import minecraft.common.world.block.PlantBlock;
import minecraft.common.world.block.PlantType;
import minecraft.common.world.block.state.BlockState;
import minecraft.common.world.gen.DiamondNoise;

public class ServerWorld extends World implements IServerWorld {
	
	public static final int NOTHING_FLAG = 0;
	public static final int BLOCK_FLAG = 1;
	public static final int STATE_FLAG = 2;
	public static final int INVENTORY_FLAG = 4;

	public ServerWorld(MinecraftClient app) {
		super(app);
	}
	
	@Override
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
				
				if (random.nextInt(80) == 0) {
					growTree(blockPos);

					blockPos.y--;
					
					setBlock(blockPos, Blocks.DIRT_BLOCK, NOTHING_FLAG);
				} else if (random.nextInt(20) == 0) {
					BlockState plantState = Blocks.PLANT_BLOCK.getDefaultState();
					
					PlantType type = PlantType.PLANT_TYPES[random.nextInt(PlantType.PLANT_TYPES.length)];
					plantState = plantState.withProperty(PlantBlock.PLANT_TYPE_PROPERTY, type);
					
					setBlockState(blockPos, plantState, NOTHING_FLAG);
				}
			}
		}
	}
	
	@Override
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
								setBlock(tmpPos, Blocks.LEAVES_BLOCK, BLOCK_FLAG + STATE_FLAG);
						}
					}
				}
			}

			if (i < treeHeight - 1) {
				tmpPos.x = blockPos.getX();
				tmpPos.z = blockPos.getZ();
				
				setBlock(tmpPos, Blocks.WOOD_LOG_BLOCK, BLOCK_FLAG + STATE_FLAG);
			}

			tmpPos.y++;
		}
	}
	
	@Override
	public void setBlockState(IBlockPosition blockPos, BlockState state, int flags) {
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
					
					if (flags != NOTHING_FLAG) {
						updateNeighbors(blockPos, state, flags);
					}
				}
			}
		}
	}
	
	@Override
	public void setBlock(IBlockPosition blockPos, Block block, int flags) {
		setBlockState(blockPos, block.getDefaultState(), flags);
	}
	
	@Override
	public void updateNeighbors(IBlockPosition blockPos, BlockState state, int flags) {
		IBlockPosition neighborPos;
		BlockState neighborState;
		
		for (Direction direction : Direction.DIRECTIONS) {
			neighborPos = blockPos.getOffset(direction);
			neighborState = getBlockState(neighborPos);
			
			if ((flags | BLOCK_FLAG) > 0) {
				neighborState.onBlockUpdate(this, neighborPos, direction.getOpposite(), state);
			}
			
			if ((flags | STATE_FLAG) > 0) {
				neighborState.onStateUpdate(this, neighborPos, direction.getOpposite(), state);
			}
			
			if ((flags | INVENTORY_FLAG) > 0) {
				neighborState.onInventoryUpdate(this, neighborPos, direction.getOpposite(), state);
			}
		}
	}
	
	private void markRangeDirty(IBlockPosition p0, IBlockPosition p1, boolean includeBorders) {
		app.getWorldRenderer().markRangeDirty(p0, p1, includeBorders);
	}

	private void markDirty(IBlockPosition blockPos, boolean includeBorders) {
		app.getWorldRenderer().markDirty(blockPos, includeBorders);
	}
	
	@Override
	public void update() {
		player.update();
		
		performRandomUpdates();
	}
	
	private void performRandomUpdates() {
		MutableBlockPosition pos = new MutableBlockPosition();

		for (int chunkX = 0; chunkX < CHUNKS_X; chunkX++) {
			for (int chunkZ = 0; chunkZ < CHUNKS_Z; chunkZ++) {
				WorldChunk chunk = getChunk(chunkX, chunkZ);
				if (chunk != null && chunk.hasRandomUpdates()) {
					for (int i = 0; i < RANDOM_TICK_SPEED; i++) {
						pos.x = random.nextInt(WorldChunk.CHUNK_SIZE) + chunkX * WorldChunk.CHUNK_SIZE;
						pos.y = random.nextInt(WORLD_HEIGHT);
						pos.z = random.nextInt(WorldChunk.CHUNK_SIZE) + chunkZ * WorldChunk.CHUNK_SIZE;

						BlockState state = chunk.getBlockState(pos);
						Block block = state.getBlock();
						
						if (block.hasRandomUpdate())
							block.randomUpdate(this, pos, state, random);
					}
				}
			}
		}
	}
}
