package minecraft.common.world.block;

import minecraft.client.renderer.model.BasicBlockModel;
import minecraft.client.renderer.model.IBlockModel;
import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.IWorld;
import minecraft.common.world.block.state.BlockState;

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
	public IBlockModel getModel(IWorld world, IBlockPosition pos, BlockState blockState) {
		return model;
	}
}
