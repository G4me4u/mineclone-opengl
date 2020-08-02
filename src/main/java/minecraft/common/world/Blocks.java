package minecraft.common.world;

import minecraft.common.world.block.Block;

public class Blocks {

	public static final Block AIR_BLOCK;
	public static final Block DIRT_BLOCK;
	public static final Block GRASS_BLOCK;
	public static final Block WOOD_PLANKS_BLOCK;
	public static final Block STONE_BLOCK;
	public static final Block COBBLESTONE_BLOCK;
	public static final Block PLANT_BLOCK;
	public static final Block LEAVES_BLOCK;
	public static final Block WOOD_LOG_BLOCK;
	
	static {
		AIR_BLOCK = Block.getBlock(Block.AIR_BLOCK_ID);
		DIRT_BLOCK = Block.getBlock(Block.DIRT_BLOCK_ID);
		GRASS_BLOCK = Block.getBlock(Block.GRASS_BLOCK_ID);
		WOOD_PLANKS_BLOCK = Block.getBlock(Block.WOOD_PLANKS_BLOCK_ID);
		STONE_BLOCK = Block.getBlock(Block.STONE_BLOCK_ID);
		COBBLESTONE_BLOCK = Block.getBlock(Block.COBBLESTONE_BLOCK_ID);
		PLANT_BLOCK = Block.getBlock(Block.PLANT_BLOCK_ID);
		LEAVES_BLOCK = Block.getBlock(Block.LEAVES_BLOCK_ID);
		WOOD_LOG_BLOCK = Block.getBlock(Block.WOOD_LOG_BLOCK_ID);
	}
}
