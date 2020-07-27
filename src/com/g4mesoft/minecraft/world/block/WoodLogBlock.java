package com.g4mesoft.minecraft.world.block;

import com.g4mesoft.minecraft.renderer.BlockTextures;
import com.g4mesoft.minecraft.renderer.tessellator.BasicBlockModel;
import com.g4mesoft.minecraft.renderer.tessellator.IBlockModel;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.state.BlockState;

public class WoodLogBlock extends Block {

	private final IBlockModel model;
	
	protected WoodLogBlock() {
		model = new BasicBlockModel(BlockTextures.OAK_LOG_TOP_TEXTURE, 
		                            BlockTextures.OAK_LOG_TOP_TEXTURE,
		                            BlockTextures.OAK_LOG_SIDE_TEXTURE);
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	public IBlockModel getModel(World world, IBlockPosition pos, BlockState blockState) {
		return model;
	}
}
