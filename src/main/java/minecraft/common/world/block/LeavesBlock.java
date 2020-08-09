package minecraft.common.world.block;

import minecraft.client.renderer.model.BasicBlockModel;
import minecraft.client.renderer.model.IBlockModel;
import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.IWorld;
import minecraft.common.world.block.state.IBlockState;

public class LeavesBlock extends Block {

	private final IBlockModel model;
	
	public LeavesBlock() {
		model = new BasicBlockModel(BlockTextures.LEAVES_TEXTURE);
	}
	
	@Override
	public IBlockModel getModel(IWorld world, IBlockPosition pos, IBlockState state) {
		return model;
	}
	
	@Override
	protected boolean hasEntityHitbox(IWorld world, IBlockPosition pos, IBlockState state) {
		return true;
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}
}
