package mineclone.common.world.block;

import java.util.NoSuchElementException;
import java.util.Set;

import mineclone.common.ReferenceRegsitry;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.signal.wire.WireType;
import mineclone.common.world.block.state.IBlockState;

public class Blocks {

	private static ReferenceRegsitry<String, IBlock> blockRegistry = null;
	private static ReferenceRegsitry<Integer, IBlockState> stateRegistry = null;
	
	public static final IBlock AIR_BLOCK;
	public static final IBlock DIRT_BLOCK;
	public static final IBlock GRASS_BLOCK;
	public static final IBlock PLANKS_BLOCK;
	public static final IBlock STONE_BLOCK;
	public static final IBlock COBBLESTONE_BLOCK;
	public static final IBlock PLANT_BLOCK;
	public static final IBlock LEAVES_BLOCK;
	public static final IBlock LOG_BLOCK;
	public static final IBlock REDSTONE_WIRE_BLOCK;
	public static final IBlock BLUESTONE_WIRE_BLOCK;
	public static final IBlock REDSTONE_BLOCK;
	public static final IBlock BLUESTONE_BLOCK;
	public static final IBlock STONE_SLAB_BLOCK;
	public static final IBlock PLANKS_SLAB_BLOCK;
	
	private static IBlock register(String name, Block block) {
		block.setName(name);

		blockRegistry.register(name, block);
	
		IBlockState state = block.getDefaultState();
		do {
			stateRegistry.register(stateRegistry.getSize(), state);
		} while ((state = state.next()) != block.getDefaultState());
		
		return block;
	}
	
	public static IBlock getBlock(String name) {
		IBlock block = blockRegistry.getElement(name);
		
		if (block == null)
			throw new NoSuchElementException("Block with name '" + name + "' does not exist!");

		return block;
	}

	public static int getIdentifier(IBlockState state) {
		return stateRegistry.getIdentifier(state);
	}
	
	public static IBlockState getState(int identifier) {
		return stateRegistry.getElement(identifier);
	}

	public static int getMaxIdentifier() {
		return stateRegistry.getSize();
	}
	
	public static Set<IBlock> getBlocks() {
		return blockRegistry.elements();
	}

	public static Set<IBlockState> getStates() {
		return stateRegistry.elements();
	}
	
	static {
		blockRegistry = new ReferenceRegsitry<>();
		stateRegistry = new ReferenceRegsitry<>();
		
		AIR_BLOCK             = register("air"             , new AirBlock());
		DIRT_BLOCK            = register("dirt"            , new DirtBlock());
		GRASS_BLOCK           = register("grass"           , new GrassBlock());
		PLANKS_BLOCK          = register("planks"          , new WoodPlanksBlock());
		STONE_BLOCK           = register("stone"           , new BasicSolidBlock());
		COBBLESTONE_BLOCK     = register("cobblestone"     , new BasicSolidBlock());
		PLANT_BLOCK           = register("plant"           , new PlantBlock());
		LEAVES_BLOCK          = register("leaves"          , new LeavesBlock());
		LOG_BLOCK             = register("log"             , new WoodLogBlock());
		REDSTONE_WIRE_BLOCK   = register("redstone_wire"   , new BasicWireBlock(WireType.REDSTONE));
		BLUESTONE_WIRE_BLOCK  = register("bluestone_wire"  , new BasicWireBlock(WireType.BLUESTONE));
		REDSTONE_BLOCK        = register("redstone_block"  , new PoweredBlock(SignalType.REDSTONE));
		BLUESTONE_BLOCK       = register("bluestone_block" , new PoweredBlock(SignalType.BLUESTONE));
		STONE_SLAB_BLOCK      = register("stone_slab"      , new StoneSlabBlock());
		PLANKS_SLAB_BLOCK     = register("planks_slab"     , new WoodPlanksSlabBlock());
	}
}
