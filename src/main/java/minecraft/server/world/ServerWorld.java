package minecraft.server.world;

import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.World;
import minecraft.common.world.WorldChunk;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.MutableBlockPosition;
import minecraft.common.world.block.PlantBlock;
import minecraft.common.world.block.PlantType;
import minecraft.common.world.block.state.IBlockState;
import minecraft.common.world.gen.DiamondNoise;

public class ServerWorld extends World implements IServerWorld {
	
	public ServerWorld() {
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
					plantState = plantState.withProperty(PlantBlock.PLANT_TYPE, type);
					
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
	public boolean setBlockState(IBlockPosition pos, IBlockState state, boolean updateNeighbors) {
		WorldChunk chunk = getChunk(pos);
		
		if (chunk != null) {
			IBlockState oldState = chunk.getBlockState(pos);

			if (chunk.setBlockState(pos, state)) {
				if (updateNeighbors) {
					if (state.isAir())
						oldState.onRemoved(this, pos);
					if (oldState.isAir())
						state.onAdded(this, pos);
					if (oldState.isOf(state.getBlock()))
						state.onStateReplaced(this, pos);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean setBlock(IBlockPosition pos, Block block, boolean updateNeighbors) {
		return setBlockState(pos, block.getDefaultState(), updateNeighbors);
	}
	
	@Override
	public void updateNeighbors(IBlockPosition blockPos, IBlockState state, int flags) {
		if ((flags & BLOCK_UPDATE_FLAG) != 0) {
			dispatchBlockUpdates(blockPos, state);
		}
		if ((flags & STATE_UPDATE_FLAG) != 0) {
			dispatchStateUpdates(blockPos, state);
		}
		if ((flags & INVENTORY_UPDATE_FLAG) != 0) {
			dispatchInventoryUpdates(blockPos, state);
		}
	}
	
	private void dispatchBlockUpdates(IBlockPosition blockPos, IBlockState state) {
		for (Direction dir : Direction.DIRECTIONS) {
			dispatchBlockUpdate(blockPos.offset(dir), dir.getOpposite(), state);
		}
	}
	
	private void dispatchStateUpdates(IBlockPosition blockPos, IBlockState state) {
		for (Direction dir : Direction.DIRECTIONS) {
			dispatchStateUpdate(blockPos.offset(dir), dir.getOpposite(), state);
		}
	}
	
	private void dispatchInventoryUpdates(IBlockPosition blockPos, IBlockState state) {
		for (Direction dir : Direction.DIRECTIONS) {
			dispatchInventoryUpdate(blockPos.offset(dir), dir.getOpposite(), state);
		}
	}
	
	@Override
	public void updateNeighbor(IBlockPosition blockPos, Direction fromDir, IBlockState neighborState, int flags) {
		if ((flags & BLOCK_UPDATE_FLAG) != 0) {
			dispatchBlockUpdate(blockPos, fromDir, neighborState);
		}
		if ((flags & STATE_UPDATE_FLAG) != 0) {
			dispatchStateUpdate(blockPos, fromDir, neighborState);
		}
		if ((flags & INVENTORY_UPDATE_FLAG) != 0) {
			dispatchInventoryUpdate(blockPos, fromDir, neighborState);
		}
	}
	
	private void dispatchBlockUpdate(IBlockPosition blockPos, Direction fromDir, IBlockState neighborState) {
		getBlockState(blockPos).onBlockUpdate(this, blockPos, fromDir, neighborState);
	}
	
	private void dispatchStateUpdate(IBlockPosition blockPos, Direction fromDir, IBlockState neighborState) {
		getBlockState(blockPos).onStateUpdate(this, blockPos, fromDir, neighborState);
	}
	
	private void dispatchInventoryUpdate(IBlockPosition blockPos, Direction fromDir, IBlockState neighborState) {
		getBlockState(blockPos).onInventoryUpdate(this, blockPos, fromDir, neighborState);
	}
	
	@Override
	public int getPower(IBlockPosition pos, int powerFlags) {
		return getPowerExceptFrom(pos, null, powerFlags);
	}

	@Override
	public int getPowerExceptFrom(IBlockPosition pos, Direction exceptDir, int powerFlags) {
		int highestPower = 0;
		
		for (Direction dir : Direction.DIRECTIONS) {
			if (dir != exceptDir) {
				int power = getPowerFrom(pos, dir, powerFlags);
				
				if (power > highestPower)
					highestPower = power;
			}
		}
		
		return highestPower;
	}
	
	@Override
	public int getPowerFrom(IBlockPosition pos, Direction dir, int powerFlags) {
		IBlockPosition neighborPos = pos.offset(dir);
		IBlockState state = getBlockState(neighborPos);
	
		if ((state.getOutputPowerFlags(dir) & powerFlags) != 0)
			return state.getPowerTo(this, neighborPos, dir.getOpposite(), powerFlags);
		
		return 0;
	}
	
	@Override
	public void update() {
		super.update();
		
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
