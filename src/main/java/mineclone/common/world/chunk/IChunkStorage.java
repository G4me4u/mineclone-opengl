package mineclone.common.world.chunk;

import java.util.Iterator;

public interface IChunkStorage<T extends IChunk> {

	public T getChunk(IChunkPosition chunkPos);

	public boolean setChunk(IChunkPosition chunkPos, T chunk);

	public boolean contains(IChunkPosition chunkPos);
	
	public Iterator<ChunkEntry<T>> chunkIterator();
	
}
