package mineclone.common.world.chunk;

public class HeightMapChunk implements IChunk {

	private final int[] heights;
	
	public HeightMapChunk() {
		heights = new int[CHUNK_SIZE * CHUNK_SIZE];
	}
	
	private int getHeightIndex(int rx, int ry) {
		return rx + (ry << CHUNK_SHIFT);
	}
	
	public int getHeight(int rx, int ry) {
		return heights[getHeightIndex(rx, ry)];
	}
	
	public boolean getHeight(int rx, int ry, int height) {
		heights[getHeightIndex(rx, ry)] = height;
		return true;
	}
}
