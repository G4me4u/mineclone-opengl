package com.g4mesoft.minecraft.world.block;

import java.util.List;

import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.state.BlockState;
import com.g4mesoft.world.phys.AABB3;

public class Block {

	private static BlockRegistry blockRegistry = null;
	
	private String name;
	private final BlockState defaultBlockState;
	
	public Block() {
		name = null;
		defaultBlockState = createDefaultState();
	}
	
	protected BlockState createDefaultState() {
		return BlockState.createStateTree(this);
	}
	
	public void getEntityHitboxes(World world, BlockPosition pos, BlockState state, List<AABB3> hitboxes) {
		if (isSolid())
			hitboxes.add(new AABB3(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1));
	}

	public boolean isSolid() {
		return false;
	}
	
	public BlockState getDefaultState() {
		return defaultBlockState;
	}
	
	public String getName() {
		return name;
	}
	
	public static final void registerBlocks() {
		if (blockRegistry != null)
			throw new IllegalStateException("Already registered blocks!");
		
		blockRegistry = new BlockRegistry();
	
		registerBlock("air", new Block());
		registerBlock("stone", new StoneBlock());
	}
	
	private static void registerBlock(String name, Block block) {
		if (block.name != null)
			throw new IllegalStateException("Block has already been registered!");
		
		blockRegistry.addBlock(name, block);
		block.name = name;
	}
	
	public static Block getBlock(String name) {
		if (blockRegistry == null)
			throw new IllegalStateException("Blocks are not yet registered!");
		
		return blockRegistry.getBlock(name);
	}
}
