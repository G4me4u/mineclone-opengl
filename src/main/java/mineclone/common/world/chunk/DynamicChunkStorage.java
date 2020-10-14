package mineclone.common.world.chunk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DynamicChunkStorage<T extends IChunk> implements IChunkStorage<T> {

	private final Map<IChunkPosition, T> chunks;
	
	public DynamicChunkStorage() {
		chunks = new HashMap<>();
	}
	
	@Override
	public T getChunk(IChunkPosition chunkPos) {
		return chunks.get(chunkPos);
	}

	@Override
	public boolean setChunk(IChunkPosition chunkPos, T chunk) {
		if (chunk == null) {
			chunks.remove(chunkPos);
		} else {
			chunks.put(chunkPos, chunk);
		}
		
		return true;
	}

	@Override
	public boolean contains(IChunkPosition chunkPos) {
		return chunks.containsKey(chunkPos);
	}

	@Override
	public Iterator<ChunkEntry<T>> chunkIterator() {
		return null;
	}
}
