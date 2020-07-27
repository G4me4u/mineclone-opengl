package com.g4mesoft.minecraft.controller;

import java.awt.event.KeyEvent;

import com.g4mesoft.Application;
import com.g4mesoft.input.key.KeyInput;
import com.g4mesoft.input.key.KeySingleInput;
import com.g4mesoft.minecraft.world.Blocks;
import com.g4mesoft.minecraft.world.block.WoodPlanksBlock;
import com.g4mesoft.minecraft.world.block.WoodType;
import com.g4mesoft.minecraft.world.block.state.BlockState;

public class PlayerHotbar {

	private final BlockState[] hotbarBlocks;
	private int hotbarIndex;
	
	private final KeyInput toggleKey;
	
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
		
		toggleKey = new KeySingleInput("hotbarToggle", KeyEvent.VK_T);
		
		Application.addKey(toggleKey);
	}
	
	public void update() {
		if (toggleKey.isClicked()) {
			hotbarIndex++;
			
			if (hotbarIndex >= hotbarBlocks.length)
				hotbarIndex = 0;
		}
	}
	
	public BlockState getHotbarBlock() {
		return hotbarBlocks[hotbarIndex];
	}
}
