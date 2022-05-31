package mineclone.client.world;

import java.util.Iterator;

import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.ChunkEntry;
import mineclone.common.world.chunk.ChunkPosition;
import mineclone.common.world.chunk.EmptyWorldChunk;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunk;
import mineclone.common.world.chunk.IWorldChunkManager;
import mineclone.common.world.chunk.StaticChunkStorage;
import mineclone.common.world.chunk.StaticHeightMap;
import mineclone.common.world.chunk.WorldChunk;

public class ClientWorldChunkManager implements IWorldChunkManager {

	private final StaticChunkStorage<IWorldChunk> storage;
	private final StaticHeightMap heightMap;
	
	public ClientWorldChunkManager() {
		storage = new StaticChunkStorage<IWorldChunk>(EmptyWorldChunk.INSTANCE);
		heightMap = new StaticHeightMap();
	}

	public void setSize(int chunkCountX, int chunkCountY, int chunkCountZ) {
		storage.setSize(chunkCountX, chunkCountY, chunkCountZ);
		heightMap.setSize(chunkCountX, chunkCountZ);
	}
	
	public IWorldChunk getChunk(IChunkPosition chunkPos, boolean initAbsent) {
		IWorldChunk chunk = storage.getChunk(chunkPos);
		if (initAbsent && chunk == EmptyWorldChunk.INSTANCE) {
			chunk = new WorldChunk();
			// Note: no need to update height map, since
			//       the entire chunk is air.
			storage.setChunk(chunkPos, chunk);
		}
		return chunk;
	}
	
	@Override
	public IWorldChunk getChunk(IChunkPosition chunkPos) {
		return getChunk(chunkPos, false);
	}

	@Override
	public boolean setChunk(IChunkPosition chunkPos, IWorldChunk chunk) {
		if (storage.setChunk(chunkPos, chunk)) {
			heightMap.updateHeightMapChunk(chunkPos, this);
			return true;
		}
		
		return false;
	}
	
	@Override
	public IBlockState getBlockState(IBlockPosition pos) {
		IWorldChunk chunk = getChunk(new ChunkPosition(pos), false);

		int rx = pos.getX() & IWorldChunk.CHUNK_MASK;
		int ry = pos.getY() & IWorldChunk.CHUNK_MASK;
		int rz = pos.getZ() & IWorldChunk.CHUNK_MASK;
		
		return chunk.getBlockState(rx, ry, rz);
	}

	@Override
	public boolean setBlockState(IBlockPosition pos, IBlockState state) {
		IWorldChunk chunk = getChunk(new ChunkPosition(pos), !state.isAir());

		int rx = pos.getX() & IWorldChunk.CHUNK_MASK;
		int ry = pos.getY() & IWorldChunk.CHUNK_MASK;
		int rz = pos.getZ() & IWorldChunk.CHUNK_MASK;
		
		if (chunk.setBlockState(rx, ry, rz, state)) {
			heightMap.updateHeightMap(pos, this);
			return true;
		}
		
		return false;
	}

	@Override
	public int getHighestPoint(int x, int z) {
		return heightMap.getHighestPoint(x, z);
	}

	@Override
	public boolean containsChunk(IChunkPosition chunkPos) {
		return storage.contains(chunkPos);
	}

	@Override
	public Iterator<ChunkEntry<IWorldChunk>> chunkIterator() {
		return storage.chunkIterator();
	}
}
