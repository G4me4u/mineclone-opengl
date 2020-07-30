package minecraft.controller;

import minecraft.input.Keyboard;
import minecraft.world.Blocks;
import minecraft.world.block.WoodPlanksBlock;
import minecraft.world.block.WoodType;
import minecraft.world.block.state.BlockState;

public class PlayerHotbar {

	private final BlockState[] hotbarBlocks;
	private int hotbarIndex;
	
	public PlayerHotbar() {
		hotbarBlocks = new BlockState[8];
		hotbarIndex = 0;
		
		hotbarBlocks[0] = Blocks.DIRT_BLOCK.getDefaultState();
		hotbarBlocks[1] = Blocks.GRASS_BLOCK.getDefaultState();
		
		BlockState plank = Blocks.WOOD_PLANKS_BLOCK.getDefaultState();
		hotbarBlocks[2] = plank.withProperty(WoodPlanksBlock.WOOD_TYPE_PROPERTY, WoodType.OAK);
		hotbarBlocks[3] = plank.withProperty(WoodPlanksBlock.WOOD_TYPE_PROPERTY, WoodType.BIRCH);
		hotbarBlocks[4] = plank.withProperty(WoodPlanksBlock.WOOD_TYPE_PROPERTY, WoodType.ACACIA);
		hotbarBlocks[5] = Blocks.STONE_BLOCK.getDefaultState();
		hotbarBlocks[6] = Blocks.COBBLESTONE_BLOCK.getDefaultState();
		hotbarBlocks[7] = Blocks.LEAVES_BLOCK.getDefaultState();
	}
	
	public void update() {
		if (Keyboard.isHeld(Keyboard.KEY_T)) {
			hotbarIndex++;
			
			if (hotbarIndex >= hotbarBlocks.length)
				hotbarIndex = 0;
		}
	}
	
	public BlockState getHotbarBlock() {
		return hotbarBlocks[hotbarIndex];
	}
}
