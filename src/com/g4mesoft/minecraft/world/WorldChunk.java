package com.g4mesoft.minecraft.world;

import java.util.Arrays;

import com.g4mesoft.minecraft.world.block.Block;
import com.g4mesoft.minecraft.world.block.IBlockPosition;
import com.g4mesoft.minecraft.world.block.state.BlockState;

public class WorldChunk {

	public static final int CHUNK_SIZE = 16;
	
	private final int chunkX;
	private final int chunkZ;

	private final BlockState[] blocks;
	private int[] heights;
	
	private int numRandomTickedBlocks;

	public WorldChunk(int chunkX, int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		
		blocks = new BlockState[CHUNK_SIZE * CHUNK_SIZE * World.WORLD_HEIGHT];
		heights = new int[CHUNK_SIZE * CHUNK_SIZE];
		
		numRandomTickedBlocks = 0;

		Arrays.fill(blocks, Blocks.AIR_BLOCK.getDefaultState());
		Arrays.fill(heights, 0);
		
		generateChunk();
	}
	
	private void generateChunk() {
		int x0 = chunkX * CHUNK_SIZE; 
		int x1 = x0 + CHUNK_SIZE; 

		int z0 = chunkZ * CHUNK_SIZE; 
		int z1 = z0 + CHUNK_SIZE; 
		
		for (int z = z0; z < z1; z++) {
			for (int x = x0; x < x1; x++) {
				for (int y = 0; y < World.WORLD_HEIGHT; y++) {
					Block block;
					if (y < World.WORLD_HEIGHT / 2) {
						block = Blocks.DIRT_BLOCK;
					} else {
						block = Blocks.AIR_BLOCK;
					}

					setBlockState(x, y, z, block.getDefaultState());
				}
			}
		}
	}
	
	private int getBlockIndex(int x, int y, int z) {
		if (y < 0 || y >= World.WORLD_HEIGHT)
			return -1;
			
		int cx = x - chunkX * CHUNK_SIZE;
		if (cx < 0 || cx >= CHUNK_SIZE)
			return -1;
		int cz = z - chunkZ * CHUNK_SIZE;
		if (cz < 0 || cz >= CHUNK_SIZE)
			return -1;

		return cx + (y + cz * World.WORLD_HEIGHT) * CHUNK_SIZE;
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

	public int getHighestPoint(IBlockPosition blockPos) {
		return getHighestPoint(blockPos.getX(), blockPos.getZ());
	}

	public int getHighestPoint(int x, int z) {
		int index = getHeightIndex(x, z);
		if (index == -1)
			return 0;
		return heights[index];
	}
	
	public BlockState getBlockState(IBlockPosition blockPos) {
		return getBlockState(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public BlockState getBlockState(int x, int y, int z) {
		int index = getBlockIndex(x, y, z);
		if (index == -1)
			return Blocks.AIR_BLOCK.getDefaultState();
		return blocks[index];
	}

	public boolean setBlockState(IBlockPosition blockPos, BlockState state) {
		return setBlockState(blockPos.getX(), blockPos.getY(), blockPos.getZ(), state);
	}

	public boolean setBlockState(int x, int y, int z, BlockState state) {
		int index = getBlockIndex(x, y, z);
		if (index == -1)
			return false;
		
		BlockState oldState = blocks[index];
		if (oldState != state) {
			blocks[index] = state;
			
			if (oldState.getBlock().isRandomTicked())
				numRandomTickedBlocks--;
			if (state.getBlock().isRandomTicked())
				numRandomTickedBlocks++;
			
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
			BlockState state = getBlockState(x, y, z);
			if (state.getBlock() != Blocks.AIR_BLOCK) {
				heights[heightIndex] = y;
				break;
			}
		}
	}
	
	public boolean isRandomTicked() {
		return numRandomTickedBlocks > 0;
	}
}
