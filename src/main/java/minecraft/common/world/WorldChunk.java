package minecraft.common.world;

import java.util.Arrays;
import java.util.Random;

import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.IBlockState;
import minecraft.common.world.gen.DiamondNoise;

public class WorldChunk {

	public static final int CHUNK_SIZE = 16;
	
	private final int chunkX;
	private final int chunkZ;

	private final IBlockState[] blocks;
	private int[] heights;
	
	private int randomUpdateCount;

	public WorldChunk(int chunkX, int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		
		blocks = new IBlockState[CHUNK_SIZE * CHUNK_SIZE * IWorld.WORLD_HEIGHT];
		heights = new int[CHUNK_SIZE * CHUNK_SIZE];
		
		randomUpdateCount = 0;

		Arrays.fill(blocks, Blocks.AIR_BLOCK.getDefaultState());
		Arrays.fill(heights, 0);
	}
	
	public void generateChunk(DiamondNoise noise, Random random) {
		int x0 = chunkX * CHUNK_SIZE; 
		int x1 = x0 + CHUNK_SIZE; 

		int z0 = chunkZ * CHUNK_SIZE; 
		int z1 = z0 + CHUNK_SIZE;
		
		for (int z = z0; z < z1; z++) {
			for (int x = x0; x < x1; x++) {
				int y1 = Math.round(16 + 86 * noise.getNoise(x, z));

				// For less height updates set top blocks first.
				setBlockState(x, y1, z, Blocks.GRASS_BLOCK.getDefaultState());

				for (int y = y1 - 1; y >= y1 - 3; y--)
					setBlockState(x, y, z, Blocks.DIRT_BLOCK.getDefaultState());
				for (int y = y1 - 4; y > 0; y--)
					setBlockState(x, y, z, Blocks.STONE_BLOCK.getDefaultState());

				setBlockState(x, 0, z, Blocks.COBBLESTONE_BLOCK.getDefaultState());
			}
		}
	}
	
	private int getBlockIndex(int x, int y, int z) {
		if (y < 0 || y >= IWorld.WORLD_HEIGHT)
			return -1;
			
		int cx = x - chunkX * CHUNK_SIZE;
		if (cx < 0 || cx >= CHUNK_SIZE)
			return -1;
		int cz = z - chunkZ * CHUNK_SIZE;
		if (cz < 0 || cz >= CHUNK_SIZE)
			return -1;

		return cx + (y + cz * IWorld.WORLD_HEIGHT) * CHUNK_SIZE;
	}
	
	private int getHeightIndex(int x, int z) {
		int cx = x - chunkX * CHUNK_SIZE;
		if (cx < 0 || cx >= CHUNK_SIZE)
			return -1;
		
		int cz = z - chunkZ * CHUNK_SIZE;
		if (cz < 0 || cz >= CHUNK_SIZE)
			return -1;
		
		return cx + cz * CHUNK_SIZE;
	}

	public int getHighestPoint(IBlockPosition pos) {
		return getHighestPoint(pos.getX(), pos.getZ());
	}

	public int getHighestPoint(int x, int z) {
		int index = getHeightIndex(x, z);
		if (index == -1)
			return 0;
		return heights[index];
	}
	
	public IBlockState getBlockState(IBlockPosition pos) {
		return getBlockState(pos.getX(), pos.getY(), pos.getZ());
	}

	public IBlockState getBlockState(int x, int y, int z) {
		int index = getBlockIndex(x, y, z);
		if (index == -1)
			return Blocks.AIR_BLOCK.getDefaultState();
		return blocks[index];
	}

	public boolean setBlockState(IBlockPosition pos, IBlockState state) {
		return setBlockState(pos.getX(), pos.getY(), pos.getZ(), state);
	}

	public boolean setBlockState(int x, int y, int z, IBlockState state) {
		int index = getBlockIndex(x, y, z);
		if (index == -1)
			return false;
		
		IBlockState oldState = blocks[index];
		
		if (oldState != state) {
			blocks[index] = state;
			
			if (oldState.getBlock().hasRandomUpdate())
				randomUpdateCount--;
			if (state.getBlock().hasRandomUpdate())
				randomUpdateCount++;
			
			updateHeight(x, z, y);

			return true;
		}

		return false;
	}
	
	private void updateHeight(int x, int z, int startHeight) {
		int heightIndex = getHeightIndex(x, z);
		if (heightIndex == -1 || heights[heightIndex] > startHeight)
			return;
		
		heights[heightIndex] = 0;
		
		for (int y = startHeight; y >= 0; y--) {
			IBlockState state = getBlockState(x, y, z);
			
			if (state.getBlock().isSolid()) {
				heights[heightIndex] = y;
				break;
			}
		}
	}
	
	public boolean hasRandomUpdates() {
		return randomUpdateCount > 0;
	}
	
	public int getChunkX() {
		return chunkX;
	}

	public int getChunkZ() {
		return chunkZ;
	}
}
