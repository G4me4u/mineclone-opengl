package mineclone.common.world.block;

import java.util.NoSuchElementException;

import mineclone.client.renderer.world.BlockTextures;
import mineclone.common.ReferenceRegsitry;

public class Blocks {

	private static ReferenceRegsitry<String, Block> blockRegistry = null;
	
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
	
	private static Block register(String name, Block block) {
		block.setName(name);

		blockRegistry.register(name, block);
	
		return block;
	}
	
	public static Block getBlock(String name) {
		Block block = blockRegistry.getElement(name);
		
		if (block == null)
			throw new NoSuchElementException("Block with name '" + name + "' does not exist!");

		return block;
	}
	
	static {
		blockRegistry = new ReferenceRegsitry<>();
		
		AIR_BLOCK             = register("air"             , new Block());
		DIRT_BLOCK            = register("dirt"            , new DirtBlock());
		GRASS_BLOCK           = register("grass"           , new GrassBlock());
		PLANKS_BLOCK          = register("planks"          , new WoodPlanksBlock());
		STONE_BLOCK           = register("stone"           , new BasicSolidBlock(BlockTextures.STONE_TEXTURE));
		COBBLESTONE_BLOCK     = register("cobblestone"     , new BasicSolidBlock(BlockTextures.COBBLESTONE_TEXTURE));
		PLANT_BLOCK           = register("plant"           , new PlantBlock());
		LEAVES_BLOCK          = register("leaves"          , new LeavesBlock());
		LOG_BLOCK             = register("log"             , new WoodLogBlock());
		REDSTONE_WIRE_BLOCK   = register("redstone_wire"   , new RedstoneWireBlock());
		REDSTONE_BLOCK        = register("redstone_block"  , new RedstoneBlock());
		STONE_SLAB_BLOCK      = register("stone_slab"      , new StoneSlabBlock());
		PLANKS_SLAB_BLOCK     = register("planks_slab"     , new WoodPlanksSlabBlock());
	}
}
