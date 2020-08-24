package minecraft.client.controller;

import minecraft.client.input.Keyboard;
import minecraft.common.world.Blocks;
import minecraft.common.world.block.StoneSlabBlock;
import minecraft.common.world.block.StoneType;
import minecraft.common.world.block.WoodPlanksBlock;
import minecraft.common.world.block.WoodPlanksSlabBlock;
import minecraft.common.world.block.WoodType;
import minecraft.common.world.block.state.IBlockState;

public class PlayerHotbar {

	private final IBlockState[] hotbarBlocks;
	private int hotbarIndex;

	private boolean wasSwitchingHotbar;
	
	public PlayerHotbar() {
		hotbarBlocks = new IBlockState[15];
		hotbarIndex = 0;
		
		hotbarBlocks[0] = Blocks.DIRT_BLOCK.getDefaultState();
		hotbarBlocks[1] = Blocks.GRASS_BLOCK.getDefaultState();
		
		IBlockState plank = Blocks.PLANKS_BLOCK.getDefaultState();
		hotbarBlocks[2] = plank.with(WoodPlanksBlock.WOOD_TYPE, WoodType.OAK);
		hotbarBlocks[3] = plank.with(WoodPlanksBlock.WOOD_TYPE, WoodType.BIRCH);
		hotbarBlocks[4] = plank.with(WoodPlanksBlock.WOOD_TYPE, WoodType.ACACIA);
		
		hotbarBlocks[5] = Blocks.STONE_BLOCK.getDefaultState();
		hotbarBlocks[6] = Blocks.COBBLESTONE_BLOCK.getDefaultState();
		hotbarBlocks[7] = Blocks.LEAVES_BLOCK.getDefaultState();
		hotbarBlocks[8] = Blocks.REDSTONE_BLOCK.getDefaultState();
		hotbarBlocks[9] = Blocks.REDSTONE_WIRE_BLOCK.getDefaultState();
		
		IBlockState stoneSlab = Blocks.STONE_SLAB_BLOCK.getDefaultState().with(StoneSlabBlock.TOP, true);
		hotbarBlocks[10] = stoneSlab.with(StoneSlabBlock.STONE_TYPE, StoneType.STONE);
		hotbarBlocks[11] = stoneSlab.with(StoneSlabBlock.STONE_TYPE, StoneType.COBBLESTONE);
		
		IBlockState planksSlab = Blocks.PLANKS_SLAB_BLOCK.getDefaultState();
		hotbarBlocks[12] = planksSlab.with(WoodPlanksSlabBlock.WOOD_TYPE, WoodType.OAK);
		hotbarBlocks[13] = planksSlab.with(WoodPlanksSlabBlock.WOOD_TYPE, WoodType.BIRCH);
		hotbarBlocks[14] = planksSlab.with(WoodPlanksSlabBlock.WOOD_TYPE, WoodType.ACACIA);
	}
	
	public void update() {
		boolean switchingHotbar = Keyboard.isHeld(Keyboard.KEY_T);
		
		if (switchingHotbar && !wasSwitchingHotbar) {
			if (Keyboard.isHeld(Keyboard.KEY_LEFT_SHIFT) || Keyboard.isHeld(Keyboard.KEY_RIGHT_SHIFT)) {
				hotbarIndex--;

				if (hotbarIndex < 0)
					hotbarIndex = hotbarBlocks.length - 1;
			} else {
				hotbarIndex++;

				if (hotbarIndex >= hotbarBlocks.length)
					hotbarIndex = 0;
			}
		}
		
		wasSwitchingHotbar = switchingHotbar;
	}
	
	public IBlockState getHotbarBlock() {
		return hotbarBlocks[hotbarIndex];
	}
}
