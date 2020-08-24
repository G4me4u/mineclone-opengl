package minecraft.common.world;

import minecraft.common.world.block.Block;

public class Blocks {

	public static final Block AIR_BLOCK;
	public static final Block DIRT_BLOCK;
	public static final Block GRASS_BLOCK;
	public static final Block PLANKS_BLOCK;
	public static final Block STONE_BLOCK;
	public static final Block COBBLESTONE_BLOCK;
	public static final Block PLANT_BLOCK;
	public static final Block LEAVES_BLOCK;
	public static final Block LOG_BLOCK;
	public static final Block REDSTONE_WIRE_BLOCK;
	public static final Block REDSTONE_BLOCK;
	public static final Block STONE_SLAB_BLOCK;
	public static final Block PLANKS_SLAB_BLOCK;
	
	static {
		AIR_BLOCK           = Block.getBlock(Block.AIR_BLOCK_ID);
		DIRT_BLOCK          = Block.getBlock(Block.DIRT_BLOCK_ID);
		GRASS_BLOCK         = Block.getBlock(Block.GRASS_BLOCK_ID);
		PLANKS_BLOCK        = Block.getBlock(Block.PLANKS_BLOCK_ID);
		STONE_BLOCK         = Block.getBlock(Block.STONE_BLOCK_ID);
		COBBLESTONE_BLOCK   = Block.getBlock(Block.COBBLESTONE_BLOCK_ID);
		PLANT_BLOCK         = Block.getBlock(Block.PLANT_BLOCK_ID);
		LEAVES_BLOCK        = Block.getBlock(Block.LEAVES_BLOCK_ID);
		LOG_BLOCK           = Block.getBlock(Block.LOG_BLOCK_ID);
		REDSTONE_WIRE_BLOCK = Block.getBlock(Block.REDSTONE_WIRE_BLOCK_ID);
		REDSTONE_BLOCK      = Block.getBlock(Block.REDSTONE_BLOCK_ID);
		STONE_SLAB_BLOCK    = Block.getBlock(Block.STONE_SLAB_BLOCK_ID);
		PLANKS_SLAB_BLOCK   = Block.getBlock(Block.PLANKS_SLAB_BLOCK_ID);
	}
}
