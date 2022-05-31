package mineclone.common.world.chunk;

import mineclone.common.world.Direction;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.MutableBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class StaticHeightMap implements IHeightMap {

	private static final int INVALID_HIGHEST_POINT = -1;
	
	private static StaticChunkStorage<HeightMapChunk> chunkStorage;
	
	public StaticHeightMap() {
		chunkStorage = new StaticChunkStorage<>(null);
	}
	
	public void setSize(int chunkCountX, int chunkCountZ) {
		chunkStorage.setSize(chunkCountX, 1, chunkCountZ);
	}
	
	private IChunkPosition makeChunkPos(int chunkX, int chunkZ) {
		return new ChunkPosition(chunkX, 0, chunkZ);
	}
	
	private IChunkPosition toChunkPos(int x, int z) {
		int chunkX = x >> IChunk.CHUNK_SHIFT;
		int chunkZ = z >> IChunk.CHUNK_SHIFT;
		return makeChunkPos(chunkX, chunkZ);
	}
	
	public void setOrigin(int chunkX, int chunkZ) {
		chunkStorage.setOrigin(makeChunkPos(chunkX, chunkZ));
	}

	public boolean contains(int x, int z) {
		return chunkStorage.contains(toChunkPos(x, z));
	}
	
	private HeightMapChunk getChunk(int x, int z, boolean initAbsent) {
		IChunkPosition chunkPos = toChunkPos(x, z);
		
		HeightMapChunk chunk = chunkStorage.getChunk(chunkPos);
		if (chunk == null) {
			if (!initAbsent || !chunkStorage.contains(chunkPos))
				return null;
			
			chunk = new HeightMapChunk();
			chunkStorage.setChunk(chunkPos, chunk);
		}
		
		return chunk;
	}	
	
	@Override
	public int getHighestPoint(int x, int z) {
		HeightMapChunk chunk = getChunk(x, z, false);
		if (chunk == null)
			return INVALID_HIGHEST_POINT;
		
		int rx = x & IChunk.CHUNK_MASK;
		int rz = z & IChunk.CHUNK_MASK;
		
		return chunk.getHeight(rx, rz);
	}
	
	public boolean updateHeightMapChunk(IChunkPosition chunkPos, IWorldChunkManager chunkManager) {
		int x0 = chunkPos.getChunkX() << IChunk.CHUNK_SHIFT;
		int y0 = chunkPos.getChunkY() << IChunk.CHUNK_SHIFT;
		int z0 = chunkPos.getChunkZ() << IChunk.CHUNK_SHIFT;

		HeightMapChunk chunk = getChunk(x0, z0, true);
		if (chunk == null)
			return false;

		IWorldChunk worldChunk = chunkManager.getChunk(chunkPos);
		for (int rz = 0; rz < IChunk.CHUNK_SIZE; rz++) {
			for (int rx = 0; rx < IChunk.CHUNK_SIZE; rx++) {
				int height = chunk.getHeight(rx, rz);
				if (height < y0 + IChunk.CHUNK_SIZE) {
					// Height will immediately by replaced by highest matching state
					// (if it exists) in the new chunk at the x- and z-coordinates.
					boolean found = false;
					for (int ry = IChunk.CHUNK_SIZE; ry-- > 0; ) {
						IBlockState state = worldChunk.getBlockState(rx, ry, rz);
						if (isMatchingState(state)) {
							height = y0 + ry;
							found = true;
							break;
						}
					}
					
					if (!found && height >= y0) {
						// Height was previously inside chunk, and new chunk does
						// not contain a matching state at the x- and z-coordinates.
						height = INVALID_HIGHEST_POINT;
						
						MutableBlockPosition mPos = new MutableBlockPosition(x0 + rx, y0, z0 + rz);
						while (mPos.y-- > 0) {
							IBlockState state = chunkManager.getBlockState(mPos);
							if (isMatchingState(state)) {
								height = mPos.y;
								break;
							}
						}
					}
					
					chunk.setHeight(rx, rz, height);
				}
			}
		}
		
		return true;
	}

	public boolean updateHeightMap(IBlockPosition pos, IWorldChunkManager chunkManager) {
		HeightMapChunk chunk = getChunk(pos.getX(), pos.getZ(), true);
		if (chunk == null)
			return false;
		
		int rx = pos.getX() & IChunk.CHUNK_MASK;
		int rz = pos.getZ() & IChunk.CHUNK_MASK;

		int height = chunk.getHeight(rx, rz);
		
		if (pos.getY() >= height) {
			IBlockState state = chunkManager.getBlockState(pos);
			if (isMatchingState(state)) {
				chunk.setHeight(rx, rz, pos.getY());
			} else if (pos.getY() == height) {
				// Only need to update the height if it was previously at pos.y.
				MutableBlockPosition mPos = new MutableBlockPosition(pos);
				while (mPos.y-- > 0) {
					state = chunkManager.getBlockState(mPos);
					if (isMatchingState(state)) {
						chunk.setHeight(rx, rz, mPos.getY());
						break;
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean isMatchingState(IBlockState state) {
		return state.isAligned(Direction.UP);
	}
}
