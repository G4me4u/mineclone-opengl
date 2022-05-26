package mineclone.common.world.chunk;

public class HeightMapChunk implements IChunk {

	private final int[] heights;
	
	public HeightMapChunk() {
		heights = new int[CHUNK_SIZE * CHUNK_SIZE];
	}
	
	private int getHeightIndex(int rx, int rz) {
		return rx + (rz << CHUNK_SHIFT);
	}
	
	public int getHeight(int rx, int rz) {
		return heights[getHeightIndex(rx, rz)];
	}
	
	public boolean setHeight(int rx, int rz, int height) {
		heights[getHeightIndex(rx, rz)] = height;
		return true;
	}
}
