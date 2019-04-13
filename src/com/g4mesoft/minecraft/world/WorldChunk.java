package com.g4mesoft.minecraft.world;

import com.g4mesoft.minecraft.world.block.IBlockPosition;
import com.g4mesoft.minecraft.world.block.state.BlockState;

public class WorldChunk {

	public static final int CHUNK_SIZE = 16;
	
	private final int chunkX;
	private final int chunkZ;
	private final BlockState[] blocks;

	public WorldChunk(int chunkX, int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		
		blocks = new BlockState[CHUNK_SIZE * CHUNK_SIZE * World.WORLD_HEIGHT];
	
		int i = 0;
		for (int z = 0; z < CHUNK_SIZE; z++) {
			for (int y = 0; y < World.WORLD_HEIGHT; y++) {
				for (int x = 0; x < CHUNK_SIZE; x++) {
					if (y < 10) {
						blocks[i++] = Blocks.STONE_BLOCK.getDefaultState();
					} else {
						blocks[i++] = Blocks.AIR_BLOCK.getDefaultState();
					}
				}
			}
		}
	}
	
	private int getBlockIndex(IBlockPosition blockPos) {
		int y = blockPos.getY();
		if (y < 0 || y >= World.WORLD_HEIGHT)
			return -1;
			
		int x = blockPos.getX() - this.chunkX * CHUNK_SIZE;
		if (x < 0 || x >= CHUNK_SIZE)
			return -1;
		int z = blockPos.getZ() - this.chunkZ * CHUNK_SIZE;
		if (z < 0 || z >= CHUNK_SIZE)
			return -1;

		return x + (y + z * World.WORLD_HEIGHT) * CHUNK_SIZE;
	}
	
	public BlockState getBlockState(IBlockPosition blockPos) {
		int index = getBlockIndex(blockPos);
		if (index == -1)
			return Blocks.AIR_BLOCK.getDefaultState();
		return blocks[index];
	}

	public boolean setBlockState(IBlockPosition blockPos, BlockState state) {
		int index = getBlockIndex(blockPos);
		if (index != -1 && blocks[index] != state) {
			blocks[index] = state;
			return true;
		}
		
		return false;
	}
}
