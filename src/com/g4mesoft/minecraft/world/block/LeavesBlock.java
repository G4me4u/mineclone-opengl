package com.g4mesoft.minecraft.world.block;

import com.g4mesoft.minecraft.renderer.BlockTextures;
import com.g4mesoft.minecraft.renderer.tessellator.BasicBlockModel;
import com.g4mesoft.minecraft.renderer.tessellator.IBlockModel;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.state.BlockState;

public class LeavesBlock extends Block {

	private final IBlockModel model;
	
	public LeavesBlock() {
		model = new BasicBlockModel(BlockTextures.LEAVES_TEXTURE);
	}
	
	@Override
	public IBlockModel getModel(World world, IBlockPosition pos, BlockState blockState) {
		return model;
	}
	
	@Override
	protected boolean hasEntityHitbox(World world, IBlockPosition pos, BlockState state) {
		return true;
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}
}
