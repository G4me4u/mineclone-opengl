package com.g4mesoft.minecraft.world.block;

import com.g4mesoft.minecraft.renderer.tessellator.BasicBlockModel;
import com.g4mesoft.minecraft.renderer.tessellator.IBlockModel;
import com.g4mesoft.minecraft.renderer.tessellator.TextureRegion;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.state.BlockState;

public class BasicSolidBlock extends Block {

	private final IBlockModel model;

	protected BasicSolidBlock(TextureRegion texture) {
		this(new BasicBlockModel(texture));
	}
	
	protected BasicSolidBlock(IBlockModel model) {
		this.model = model;
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
