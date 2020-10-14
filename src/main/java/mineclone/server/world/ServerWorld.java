package mineclone.server.world;

import java.util.Iterator;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.World;
import mineclone.common.world.block.Block;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.MutableBlockPosition;
import mineclone.common.world.block.PlantBlock;
import mineclone.common.world.block.PlantType;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.WorldChunk;
import mineclone.common.world.chunk.ChunkEntry;
import mineclone.common.world.chunk.ChunkPosition;
import mineclone.common.world.chunk.IWorldChunk;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.gen.DiamondNoise;

public class ServerWorld extends World implements IServerWorld {

	private static final int RANDOM_TICK_SPEED = 3;
	
	public ServerWorld() {
	}
	
	@Override
	public void generateWorld() {
		DiamondNoise noise = new DiamondNoise(CHUNKS_X * IWorldChunk.CHUNK_SIZE, random);
		
		for (int cz = 0; cz < CHUNKS_Z; cz++) {
			for (int cy = 0; cy < CHUNKS_Y; cy++) {
				for (int cx = 0; cx < CHUNKS_X; cx++)
					chunkManager.setChunk(new ChunkPosition(cx, cy, cz), new WorldChunk());
			}
		}

		for (int chunkZ = 0; chunkZ < CHUNKS_X; chunkZ++) {
			for (int chunkX = 0; chunkX < CHUNKS_X; chunkX++)
				generateChunk(chunkX, chunkZ, noise);
		}
		
		for (int chunkZ = 0; chunkZ < CHUNKS_X; chunkZ++) {
			for (int chunkX = 0; chunkX < CHUNKS_X; chunkX++)
				populateChunk(chunkX, chunkZ);
		}
	}
	
	private void generateChunk(int chunkX, int chunkZ, DiamondNoise noise) {
		int x0 = chunkX * IWorldChunk.CHUNK_SIZE; 
		int x1 = x0 + IWorldChunk.CHUNK_SIZE; 

		int z0 = chunkZ * IWorldChunk.CHUNK_SIZE; 
		int z1 = z0 + IWorldChunk.CHUNK_SIZE;
		
		for (int z = z0; z < z1; z++) {
			for (int x = x0; x < x1; x++) {
				int y1 = Math.round(32 + 32 * noise.getNoise(x, z));

				setBlock(new MutableBlockPosition(x, y1, z), Blocks.GRASS_BLOCK, false);

				for (int y = y1 - 1; y >= y1 - 3; y--)
					setBlock(new MutableBlockPosition(x, y, z), Blocks.DIRT_BLOCK, false);
				for (int y = y1 - 4; y > 0; y--)
					setBlock(new MutableBlockPosition(x, y, z), Blocks.STONE_BLOCK, false);

				setBlock(new MutableBlockPosition(x, 0, z), Blocks.COBBLESTONE_BLOCK, false);
			}
		}
	}
	
	private void populateChunk(int chunkX, int chunkZ) {
		MutableBlockPosition pos = new MutableBlockPosition();
		
		int x0 = chunkX * IWorldChunk.CHUNK_SIZE;
		int z0 = chunkZ * IWorldChunk.CHUNK_SIZE;
		int x1 = x0 + IWorldChunk.CHUNK_SIZE;
		int z1 = z0 + IWorldChunk.CHUNK_SIZE;
		
		for (pos.z = z0; pos.z < z1; pos.z++) {
			for (pos.x = x0; pos.x < x1; pos.x++) {
				pos.y = getHighestPoint(pos) + 1;
				
				if (random.nextInt(80) == 0) {
					growTree(pos);

					pos.y--;
					
					setBlock(pos, Blocks.DIRT_BLOCK, false);
				} else if (random.nextInt(20) == 0) {
					IBlockState plantState = Blocks.PLANT_BLOCK.getDefaultState();
					
					PlantType type = PlantType.TYPES[random.nextInt(PlantType.TYPES.length)];
					plantState = plantState.with(PlantBlock.PLANT_TYPE, type);
					
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
				
				setBlock(tmpPos, Blocks.LOG_BLOCK, true);
			}

			tmpPos.y++;
		}
	}
	
	@Override
	public boolean setBlockState(IBlockPosition pos, IBlockState newState, boolean updateNeighbors) {
		IBlockState oldState = chunkManager.getBlockState(pos);

		if (chunkManager.setBlockState(pos, newState)) {
			if (updateNeighbors) {
				Block oldBlock = oldState.getBlock();
				Block newBlock = newState.getBlock();
				
				if (oldBlock == newBlock) {
					newBlock.onStateChanged(this, pos, oldState, newState);
				} else {
					oldBlock.onBlockRemoved(this, pos, oldState);
					newBlock.onBlockAdded(this, pos, newState);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean setBlock(IBlockPosition pos, Block newBlock, boolean updateNeighbors) {
		return setBlockState(pos, newBlock.getDefaultState(), updateNeighbors);
	}
	
	@Override
	public void updateNeighbors(IBlockPosition pos, int flags) {
		IBlockState fromState = getBlockState(pos);
		
		if ((flags & STATE_UPDATE_FLAG) != 0)
			dispatchStateUpdates(pos, fromState);

		if ((flags & BLOCK_UPDATE_FLAG) != 0)
			dispatchBlockUpdates(pos, fromState);
		
		if ((flags & INVENTORY_UPDATE_FLAG) != 0)
			dispatchInventoryUpdates(pos, fromState);
	}
	
	private void dispatchStateUpdates(IBlockPosition pos, IBlockState fromState) {
		for (Direction dir : Direction.DIRECTIONS)
			dispatchStateUpdate(pos.offset(dir), dir.getOpposite(), fromState);
	}
	
	private void dispatchBlockUpdates(IBlockPosition pos, IBlockState fromState) {
		for (Direction dir : Direction.DIRECTIONS)
			dispatchBlockUpdate(pos.offset(dir), dir.getOpposite(), fromState);
	}

	private void dispatchInventoryUpdates(IBlockPosition pos, IBlockState fromState) {
		for (Direction dir : Direction.DIRECTIONS)
			dispatchInventoryUpdate(pos.offset(dir), dir.getOpposite(), fromState);
	}
	
	@Override
	public void updateNeighbor(IBlockPosition pos, Direction fromDir, IBlockState fromState, int updateFlags) {
		if ((updateFlags & STATE_UPDATE_FLAG) != 0)
			dispatchStateUpdate(pos, fromDir, fromState);

		if ((updateFlags & BLOCK_UPDATE_FLAG) != 0)
			dispatchBlockUpdate(pos, fromDir, fromState);

		if ((updateFlags & INVENTORY_UPDATE_FLAG) != 0)
			dispatchInventoryUpdate(pos, fromDir, fromState);
	}
	
	private void dispatchStateUpdate(IBlockPosition pos, Direction fromDir, IBlockState fromState) {
		getBlockState(pos).onStateUpdate(this, pos, fromDir, fromState);
	}

	private void dispatchBlockUpdate(IBlockPosition pos, Direction fromDir, IBlockState fromState) {
		getBlockState(pos).onBlockUpdate(this, pos, fromDir, fromState);
	}
	
	private void dispatchInventoryUpdate(IBlockPosition pos, Direction fromDir, IBlockState fromState) {
		getBlockState(pos).onInventoryUpdate(this, pos, fromDir, fromState);
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
	
		return state.getPowerTo(this, neighborPos, dir.getOpposite(), powerFlags);
	}
	
	@Override
	public void update() {
		super.update();
		
		performRandomUpdates();
	}
	
	private void performRandomUpdates() {
		MutableBlockPosition pos = new MutableBlockPosition();

		Iterator<ChunkEntry<IWorldChunk>> itr = chunkManager.chunkIterator();
		while (itr.hasNext()) {
			ChunkEntry<IWorldChunk> entry = itr.next();
			
			IChunkPosition chunkPos = entry.getChunkPos();
			IWorldChunk chunk = entry.getChunk();
			
			if (chunk.hasRandomUpdates()) {
				for (int i = 0; i < RANDOM_TICK_SPEED; i++) {
					int rx = random.nextInt(IWorldChunk.CHUNK_SIZE);
					int ry = random.nextInt(IWorldChunk.CHUNK_SIZE);
					int rz = random.nextInt(IWorldChunk.CHUNK_SIZE);
					
					pos.x = rx + (chunkPos.getChunkX() << IWorldChunk.CHUNK_SHIFT);
					pos.y = ry + (chunkPos.getChunkY() << IWorldChunk.CHUNK_SHIFT);
					pos.z = rz + (chunkPos.getChunkZ() << IWorldChunk.CHUNK_SHIFT);
	
					IBlockState state = chunk.getBlockState(rx, ry, rz);
					
					if (state.hasRandomUpdate())
						state.onRandomUpdate(this, pos, random);
				}
			}
		}
	}
}
