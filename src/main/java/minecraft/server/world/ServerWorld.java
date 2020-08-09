package minecraft.server.world;

import java.util.ArrayList;
import java.util.List;

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
import minecraft.common.world.block.state.IBlockState;
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
		MutableBlockPosition pos = new MutableBlockPosition();
		
		int x0 = chunk.getChunkX() * WorldChunk.CHUNK_SIZE;
		int z0 = chunk.getChunkZ() * WorldChunk.CHUNK_SIZE;
		int x1 = x0 + WorldChunk.CHUNK_SIZE;
		int z1 = z0 + WorldChunk.CHUNK_SIZE;
		
		for (pos.z = z0; pos.z < z1; pos.z++) {
			for (pos.x = x0; pos.x < x1; pos.x++) {
				pos.y = chunk.getHighestPoint(pos) + 1;
				
				if (random.nextInt(80) == 0) {
					growTree(pos);

					pos.y--;
					
					setBlock(pos, Blocks.DIRT_BLOCK, false);
				} else if (random.nextInt(20) == 0) {
					IBlockState plantState = Blocks.PLANT_BLOCK.getDefaultState();
					
					PlantType type = PlantType.TYPES[random.nextInt(PlantType.TYPES.length)];
					plantState = plantState.withProperty(PlantBlock.PLANT_TYPE_PROPERTY, type);
					
					setBlockState(pos, plantState, false);
				}
			}
		}
	}
	
	@Override
	public void growTree(IBlockPosition pos) {
		int treeHeight = 5 + random.nextInt(3);
		int trunkHeight = Math.max(1, treeHeight - 5);
		
		MutableBlockPosition tmpPos = new MutableBlockPosition(pos);
		
		for (int i = 0; i < treeHeight; i++) {
			if (i > trunkHeight) {
				int yo = i - trunkHeight;
				
				for (int zo = -3; zo <= 3; zo++) {
					for (int xo = -3; xo <= 3; xo++) {
						tmpPos.x = pos.getX() + xo;
						tmpPos.z = pos.getZ() + zo;
						
						if (getBlock(tmpPos) == Blocks.AIR_BLOCK) {
							int distSqr = xo * xo + yo * yo + zo * zo;
							
							if (distSqr < 3 * 2 * 3)
								setBlock(tmpPos, Blocks.LEAVES_BLOCK, true);
						}
					}
				}
			}

			if (i < treeHeight - 1) {
				tmpPos.x = pos.getX();
				tmpPos.z = pos.getZ();
				
				setBlock(tmpPos, Blocks.WOOD_LOG_BLOCK, true);
			}

			tmpPos.y++;
		}
	}
	
	@Override
	public void setBlockState(IBlockPosition pos, IBlockState state, boolean updateNeighbors) {
		WorldChunk chunk = getChunk(pos);
		
		if (chunk != null) {
			IBlockState oldState = chunk.getBlockState(pos);

			if (oldState != state) {
				int oldHighestPoint = chunk.getHighestPoint(pos);
				
				if (chunk.setBlockState(pos, state)) {
					int highestPoint = chunk.getHighestPoint(pos);
					
					if (oldHighestPoint != highestPoint) {
						int x = pos.getX();
						int z = pos.getZ();
						
						markRangeDirty(new ImmutableBlockPosition(x, oldHighestPoint, z), 
						               new ImmutableBlockPosition(x,    highestPoint, z), true);
					} else {
						Block oldBlock = oldState.getBlock();
						Block newBlock = state.getBlock();
						
						if (oldBlock.isSolid() || newBlock.isSolid()) {
							markDirty(pos, (oldBlock != newBlock));
						} else {
							markDirty(pos, false);
						}
					}
					
					if (updateNeighbors) {
						if (state.isAir())
							oldState.onRemoved(this, pos);
						if (oldState.isAir())
							state.onAdded(this, pos);
						if (oldState.isOf(state.getBlock()))
							state.onStateReplaced(this, pos);
					}
				}
			}
		}
	}
	
	@Override
	public void setBlock(IBlockPosition pos, Block block, boolean updateNeighbors) {
		setBlockState(pos, block.getDefaultState(), updateNeighbors);
	}
	
	@Override
	public void updateNeighbors(IBlockPosition pos, IBlockState state, int flags) {
		List<Integer> usedFlags = new ArrayList<>();
		if ((flags | BLOCK_FLAG) != 0) {
			usedFlags.add(BLOCK_FLAG);
		}
		if ((flags | STATE_FLAG) != 0) {
			usedFlags.add(STATE_FLAG);
		}
		if ((flags | INVENTORY_FLAG) != 0) {
			usedFlags.add(INVENTORY_FLAG);
		}
		
		for (int flag : usedFlags) {
			for (Direction dir : Direction.DIRECTIONS) {
				updateNeighbor(pos.offset(dir), dir.getOpposite(), state, flag);
			}
		}
	}
	
	@Override
	public void updateNeighbor(IBlockPosition pos, Direction fromDir, IBlockState neighborState, int flag) {
		IBlockState state = getBlockState(pos);
		
		switch (flag) {
		case BLOCK_FLAG:
			state.onBlockUpdate(this, pos, fromDir, neighborState);
			break;
		case STATE_FLAG:
			state.onStateUpdate(this, pos, fromDir, neighborState);
			break;
		case INVENTORY_FLAG:
			state.onInventoryUpdate(this, pos, fromDir, neighborState);
			break;
		default:
			break;
		}
	}
	
	private void markRangeDirty(IBlockPosition p0, IBlockPosition p1, boolean includeBorders) {
		app.getWorldRenderer().markRangeDirty(p0, p1, includeBorders);
	}

	private void markDirty(IBlockPosition pos, boolean includeBorders) {
		app.getWorldRenderer().markDirty(pos, includeBorders);
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

						IBlockState state = chunk.getBlockState(pos);
						
						if (state.getBlock().hasRandomUpdate())
							state.onRandomUpdate(this, pos, random);
					}
				}
			}
		}
	}
}
