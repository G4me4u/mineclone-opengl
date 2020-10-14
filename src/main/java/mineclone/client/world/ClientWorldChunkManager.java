package mineclone.client.world;

import java.util.Iterator;

import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.BasicHeightMap;
import mineclone.common.world.chunk.ChunkEntry;
import mineclone.common.world.chunk.ChunkPosition;
import mineclone.common.world.chunk.EmptyWorldChunk;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunk;
import mineclone.common.world.chunk.IWorldChunkManager;
import mineclone.common.world.chunk.StaticChunkStorage;

public class ClientWorldChunkManager implements IWorldChunkManager {

	private final StaticChunkStorage<IWorldChunk> storage;
	private final BasicHeightMap heightMap;
	
	public ClientWorldChunkManager() {
		storage = new StaticChunkStorage<IWorldChunk>();
		heightMap = new BasicHeightMap();
	}
	
	public IWorldChunk getChunk(IChunkPosition chunkPos) {
		IWorldChunk chunk = storage.getChunk(chunkPos);
		return (chunk == null) ? EmptyWorldChunk.INSTANCE : chunk;
	}

	public boolean setChunk(IChunkPosition chunkPos, IWorldChunk chunk) {
		return storage.setChunk(chunkPos, chunk);
	}
	
	@Override
	public IBlockState getBlockState(IBlockPosition pos) {
		IWorldChunk chunk = getChunk(new ChunkPosition(pos));

		int rx = pos.getX() & IWorldChunk.CHUNK_MASK;
		int ry = pos.getY() & IWorldChunk.CHUNK_MASK;
		int rz = pos.getZ() & IWorldChunk.CHUNK_MASK;
		
		return chunk.getBlockState(rx, ry, rz);
	}

	@Override
	public boolean setBlockState(IBlockPosition pos, IBlockState state) {
		IWorldChunk chunk = getChunk(new ChunkPosition(pos));

		int rx = pos.getX() & IWorldChunk.CHUNK_MASK;
		int ry = pos.getY() & IWorldChunk.CHUNK_MASK;
		int rz = pos.getZ() & IWorldChunk.CHUNK_MASK;
		
		return chunk.setBlockState(rx, ry, rz, state);
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
