package com.g4mesoft.minecraft.world.block;

import java.util.List;
import java.util.Random;

import com.g4mesoft.minecraft.renderer.tessellator.IBlockModel;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.state.BlockState;
import com.g4mesoft.world.phys.AABB3;

public class Block {

	public static final String AIR_BLOCK_ID = "air";
	public static final String DIRT_BLOCK_ID = "dirt";
	public static final String GRASS_BLOCK_ID = "grass";
	public static final String WOOD_PLANKS_BLOCK_ID = "planks";
	
	private static BlockRegistry blockRegistry = null;
	
	private String name;
	private final BlockState defaultBlockState;
	
	protected Block() {
		name = null;
		defaultBlockState = createDefaultState();
	}
	
	protected BlockState createDefaultState() {
		return BlockState.createStateTree(this);
	}
	
	public void getEntityHitboxes(World world, IBlockPosition pos, BlockState state, List<AABB3> hitboxes) {
		if (isSolid()) {
			float x = pos.getX();
			float y = pos.getY();
			float z = pos.getZ();
			
			hitboxes.add(new AABB3(x, y, z, x + 1.0f, y + 1.0f, z + 1.0f));
		}
	}

	public IBlockModel getModel(World world, IBlockPosition pos, BlockState blockState) {
		return null;
	}
	
	public boolean isSolid() {
		return false;
	}
	
	public void randomTick(World world, IBlockPosition pos, BlockState blockState, Random random) {
	}
	
	public boolean isRandomTicked() {
		return false;
	}
	
	public BlockState getDefaultState() {
		return defaultBlockState;
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
		
		blockRegistry = new BlockRegistry();
	
		registerBlock(Block.AIR_BLOCK_ID, new Block());
		registerBlock(Block.DIRT_BLOCK_ID, new DirtBlock());
		registerBlock(Block.GRASS_BLOCK_ID, new GrassBlock());
		registerBlock(Block.WOOD_PLANKS_BLOCK_ID, new WoodPlanksBlock());
	}
	
	private static void registerBlock(String name, Block block) {
		if (block.getName() != null)
			throw new IllegalStateException("Block has already been registered!");
		
		blockRegistry.addBlock(name, block);
		
		block.setName(name);
	}
	
	public static Block getBlock(String name) {
		if (blockRegistry == null)
			throw new IllegalStateException("Blocks are not yet registered!");
		
		return blockRegistry.getBlock(name);
	}
}
