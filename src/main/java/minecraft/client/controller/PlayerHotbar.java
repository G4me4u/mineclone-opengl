package minecraft.client.controller;

import minecraft.client.input.Keyboard;
import minecraft.common.world.Blocks;
import minecraft.common.world.block.WoodPlanksBlock;
import minecraft.common.world.block.WoodType;
import minecraft.common.world.block.state.IBlockState;

public class PlayerHotbar {

	private final IBlockState[] hotbarBlocks;
	private int hotbarIndex;

	private boolean wasSwitchingHotbar;
	
	public PlayerHotbar() {
		hotbarBlocks = new IBlockState[9];
		hotbarIndex = 0;
		
		hotbarBlocks[0] = Blocks.DIRT_BLOCK.getDefaultState();
		hotbarBlocks[1] = Blocks.GRASS_BLOCK.getDefaultState();
		
		IBlockState plank = Blocks.WOOD_PLANKS_BLOCK.getDefaultState();
		hotbarBlocks[2] = plank.with(WoodPlanksBlock.WOOD_TYPE, WoodType.OAK);
		hotbarBlocks[3] = plank.with(WoodPlanksBlock.WOOD_TYPE, WoodType.BIRCH);
		hotbarBlocks[4] = plank.with(WoodPlanksBlock.WOOD_TYPE, WoodType.ACACIA);
		hotbarBlocks[5] = Blocks.STONE_BLOCK.getDefaultState();
		hotbarBlocks[6] = Blocks.COBBLESTONE_BLOCK.getDefaultState();
		hotbarBlocks[7] = Blocks.LEAVES_BLOCK.getDefaultState();
		hotbarBlocks[8] = Blocks.REDSTONE_BLOCK.getDefaultState();
	}
	
	public void update() {
		boolean switchingHotbar = Keyboard.isHeld(Keyboard.KEY_T);
		
		if (switchingHotbar && !wasSwitchingHotbar) {
			hotbarIndex++;
			
			if (hotbarIndex >= hotbarBlocks.length)
				hotbarIndex = 0;
		}
		
		wasSwitchingHotbar = switchingHotbar;
	}
	
	public IBlockState getHotbarBlock() {
		return hotbarBlocks[hotbarIndex];
	}
}
