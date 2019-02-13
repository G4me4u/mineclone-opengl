package com.g4mesoft.minecraft.world;

import com.g4mesoft.minecraft.world.block.Block;

public class Blocks {

	public static final Block AIR_BLOCK;
	public static final Block STONE_BLOCK;
	
	static {
		AIR_BLOCK = Block.getBlock("air");
		STONE_BLOCK = Block.getBlock("stone");
	}
}
