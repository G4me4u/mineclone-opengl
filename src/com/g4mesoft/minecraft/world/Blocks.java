package com.g4mesoft.minecraft.world;

import com.g4mesoft.minecraft.world.block.Block;

public class Blocks {

	public static final Block AIR_BLOCK;
	public static final Block DIRT_BLOCK;
	public static final Block GRASS_BLOCK;
	public static final Block WOOD_PLANKS_BLOCK;
	
	static {
		AIR_BLOCK = Block.getBlock(Block.AIR_BLOCK_ID);
		DIRT_BLOCK = Block.getBlock(Block.DIRT_BLOCK_ID);
		GRASS_BLOCK = Block.getBlock(Block.GRASS_BLOCK_ID);
		WOOD_PLANKS_BLOCK = Block.getBlock(Block.WOOD_PLANKS_BLOCK_ID);
	}
}
