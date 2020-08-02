package minecraft.client.controller;

import minecraft.client.input.Keyboard;
import minecraft.common.world.Blocks;
import minecraft.common.world.block.WoodPlanksBlock;
import minecraft.common.world.block.WoodType;
import minecraft.common.world.block.state.BlockState;

public class PlayerHotbar {

	private final BlockState[] hotbarBlocks;
	private int hotbarIndex;

	private boolean wasSwitchingHotbar;
	
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
		boolean switchingHotbar = Keyboard.isHeld(Keyboard.KEY_T);
		
		if (switchingHotbar && !wasSwitchingHotbar) {
			hotbarIndex++;
			
			if (hotbarIndex >= hotbarBlocks.length)
				hotbarIndex = 0;
		}
		
		wasSwitchingHotbar = switchingHotbar;
	}
	
	public BlockState getHotbarBlock() {
		return hotbarBlocks[hotbarIndex];
	}
}
