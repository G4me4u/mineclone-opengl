package mineclone.common.world.block;

import mineclone.client.renderer.model.BasicBlockModel;
import mineclone.client.renderer.model.IBlockModel;
import mineclone.client.renderer.world.BlockTextures;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.IBlockState;

public class WoodLogBlock extends Block {

	private final IBlockModel model;
	
	protected WoodLogBlock() {
		model = new BasicBlockModel(BlockTextures.OAK_LOG_TOP_TEXTURE, 
		                            BlockTextures.OAK_LOG_TOP_TEXTURE,
		                            BlockTextures.OAK_LOG_SIDE_TEXTURE);
	}

	@Override
	public IBlockModel getModel(IWorld world, IBlockPosition pos, IBlockState state) {
		return model;
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
}
