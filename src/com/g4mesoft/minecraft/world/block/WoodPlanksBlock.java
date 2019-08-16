package com.g4mesoft.minecraft.world.block;

import com.g4mesoft.minecraft.renderer.BlockTextures;
import com.g4mesoft.minecraft.renderer.tessellator.BasicBlockModel;
import com.g4mesoft.minecraft.renderer.tessellator.IBlockModel;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.state.BlockState;
import com.g4mesoft.minecraft.world.block.state.EnumBlockProperty;
import com.g4mesoft.minecraft.world.block.state.IBlockProperty;

public class WoodPlanksBlock extends Block {

	public static final IBlockProperty<WoodType> WOOD_TYPE_PROPERTY = 
			new EnumBlockProperty<WoodType>("type", WoodType.WOOD_TYPES);
	
	private final IBlockModel[] models;
	
	public WoodPlanksBlock() {
		models = new IBlockModel[WoodType.WOOD_TYPES.length];
		
		models[WoodType.OAK.getIndex()] = new BasicBlockModel(BlockTextures.OAK_PLANKS_TEXTURE);
		models[WoodType.BIRCH.getIndex()] = new BasicBlockModel(BlockTextures.BIRCH_PLANKS_TEXTURE);
		models[WoodType.ACACIA.getIndex()] = new BasicBlockModel(BlockTextures.ACACIA_PLANKS_TEXTURE);
	}
	
	@Override
	public IBlockModel getModel(World world, IBlockPosition pos, BlockState blockState) {
		WoodType type = blockState.getValue(WOOD_TYPE_PROPERTY);
		
		int index = type.getIndex();
		if (index >= models.length)
			index = 0;
		
		return models[index];
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	protected BlockState createDefaultState() {
		return BlockState.createStateTree(this, WOOD_TYPE_PROPERTY);
	}
}
