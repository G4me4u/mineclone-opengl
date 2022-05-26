package mineclone.common.world.chunk;

public interface IChunk {

	public static final int CHUNK_SIZE  = 16;
	public static final int CHUNK_SHIFT =  4;
	public static final int CHUNK_MASK  = 15;
	
	public static final int CHUNK_VOLUME = CHUNK_SIZE * CHUNK_SIZE;
	
}
