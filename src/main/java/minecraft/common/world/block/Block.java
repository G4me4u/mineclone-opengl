package minecraft.common.world.block;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import minecraft.client.renderer.model.IBlockModel;
import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.ReferenceRegsitry;
import minecraft.common.world.Direction;
import minecraft.common.world.EntityHitbox;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.IWorld;
import minecraft.common.world.block.state.BlockState;
import minecraft.server.world.ServerWorld;

public class Block {

	public static final String AIR_BLOCK_ID         	= "air";
	public static final String DIRT_BLOCK_ID        	= "dirt";
	public static final String GRASS_BLOCK_ID       	= "grass";
	public static final String WOOD_PLANKS_BLOCK_ID 	= "planks";
	public static final String STONE_BLOCK_ID       	= "stone";
	public static final String COBBLESTONE_BLOCK_ID 	= "cobblestone";
	public static final String PLANT_BLOCK_ID       	= "plant";
	public static final String LEAVES_BLOCK_ID      	= "leaves";
	public static final String WOOD_LOG_BLOCK_ID    	= "log";
	public static final String REDSTONE_WIRE_BLOCK_ID	= "redstone_wire";
	
	private static ReferenceRegsitry<String, Block> blockRegistry = null;
	
	private String name;
	private final BlockState defaultBlockState;
	
	protected Block() {
		name = null;
		defaultBlockState = createDefaultState();
	}
	
	protected BlockState createDefaultState() {
		return BlockState.createStateTree(this);
	}
	
	public void onAdded(BlockState state, IServerWorld world, IBlockPosition blockPos) {
		world.updateNeighbors(blockPos, state, ServerWorld.BLOCK_FLAG + ServerWorld.STATE_FLAG);
	}
	
	public void onRemoved(BlockState state, IServerWorld world, IBlockPosition blockPos) {
		world.updateNeighbors(blockPos, state, ServerWorld.BLOCK_FLAG + ServerWorld.STATE_FLAG);
	}
	
	public void onStateReplaced(BlockState state, IServerWorld world, IBlockPosition blockPos) {
		
	}
	
	public void getEntityHitboxes(IWorld world, IBlockPosition pos, BlockState state, List<EntityHitbox> hitboxes) {
		if (hasEntityHitbox(world, pos, state)) {
			float x = pos.getX();
			float y = pos.getY();
			float z = pos.getZ();
			
			hitboxes.add(new EntityHitbox(x, y, z, x + 1.0f, y + 1.0f, z + 1.0f));
		}
	}

	protected boolean hasEntityHitbox(IWorld world, IBlockPosition pos, BlockState state) {
		return isSolid();
	}

	public IBlockModel getModel(IWorld world, IBlockPosition pos, BlockState blockState) {
		return null;
	}
	
	public boolean isSolid() {
		return false;
	}
	
	public boolean canGrowVegetation(BlockState state) {
		return false;
	}
	
	public boolean conductsRedstonePower(BlockState state) {
		return isSolid();
	}
	
	public boolean connectsToRedstoneWire(BlockState state, Direction direction) {
		return false;
	}
	
	public int getPower(BlockState state, IServerWorld world, IBlockPosition blockPos, Direction direction, int type) {
		return 0;
	}
	
	public void randomUpdate(IServerWorld world, IBlockPosition pos, BlockState blockState, Random random) {
	}
	
	public boolean hasRandomUpdate() {
		return false;
	}
	
	public BlockState getDefaultState() {
		return defaultBlockState;
	}
	
	public void onBlockUpdate(BlockState state, IServerWorld world, IBlockPosition blockPos, Direction direction, BlockState neighborState) {
		
	}
	
	public void onStateUpdate(BlockState state, IServerWorld world, IBlockPosition blockPos, Direction direction, BlockState neighborState) {
		
	}
	
	public void onInventoryUpdate(BlockState state, IServerWorld world, IBlockPosition blockPos, Direction direction, BlockState neighborState) {
		
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}
	
	public static final void registerBlocks() {
		if (blockRegistry != null)
			throw new IllegalStateException("Already registered blocks!");
		
		blockRegistry = new ReferenceRegsitry<>();
	
		registerBlock(AIR_BLOCK_ID, new Block());
		registerBlock(DIRT_BLOCK_ID, new DirtBlock());
		registerBlock(GRASS_BLOCK_ID, new GrassBlock());
		registerBlock(WOOD_PLANKS_BLOCK_ID, new WoodPlanksBlock());
		registerBlock(STONE_BLOCK_ID, new BasicSolidBlock(BlockTextures.STONE_TEXTURE));
		registerBlock(COBBLESTONE_BLOCK_ID, new BasicSolidBlock(BlockTextures.COBBLESTONE_TEXTURE));
		registerBlock(PLANT_BLOCK_ID, new PlantBlock());
		registerBlock(LEAVES_BLOCK_ID, new LeavesBlock());
		registerBlock(WOOD_LOG_BLOCK_ID, new WoodLogBlock());
	}
	
	private static void registerBlock(String name, Block block) {
		blockRegistry.register(name, block);
		
		block.setName(name);
	}
	
	public static Block getBlock(String name) {
		if (blockRegistry == null)
			throw new IllegalStateException("Blocks are not yet registered!");
		
		Block block = blockRegistry.getElement(name);
		
		if (block == null)
			throw new NoSuchElementException("Block '" + name + "' is not registered.");

		return block;
	}
}
