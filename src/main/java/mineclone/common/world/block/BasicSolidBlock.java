package mineclone.common.world.block;

import mineclone.client.graphic.ITextureRegion;
import mineclone.client.renderer.model.BasicBlockModel;
import mineclone.client.renderer.model.IBlockModel;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.IBlockState;

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
