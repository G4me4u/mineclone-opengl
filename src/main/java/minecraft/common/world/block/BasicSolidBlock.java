package minecraft.common.world.block;

import minecraft.client.graphic.ITextureRegion;
import minecraft.client.renderer.model.BasicBlockModel;
import minecraft.client.renderer.model.IBlockModel;
import minecraft.common.world.IWorld;
import minecraft.common.world.block.state.IBlockState;

public class BasicSolidBlock extends Block {

	private final IBlockModel model;

	protected BasicSolidBlock(ITextureRegion texture) {
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
	public IBlockModel getModel(IWorld world, IBlockPosition pos, IBlockState state) {
		return model;
	}
}
