package minecraft.world.block;

import minecraft.renderer.model.BasicBlockModel;
import minecraft.renderer.model.IBlockModel;
import minecraft.renderer.world.BlockTextures;
import minecraft.world.World;
import minecraft.world.block.state.BlockState;

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
