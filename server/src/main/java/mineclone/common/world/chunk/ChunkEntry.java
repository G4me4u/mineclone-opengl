package mineclone.common.world.chunk;

public class ChunkEntry<T> {

	private final IChunkPosition chunkPos;
	private final T chunk;
	
	public ChunkEntry(IChunkPosition chunkPos, T chunk) {
		this.chunkPos = chunkPos;
		this.chunk = chunk;
	}
	
	public IChunkPosition getChunkPos() {
		return chunkPos;
	}
	
	public T getChunk() {
		return chunk;
	}
}
