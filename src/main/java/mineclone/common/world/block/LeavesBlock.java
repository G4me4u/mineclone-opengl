package mineclone.common.world.block;

import mineclone.client.renderer.model.BasicBlockModel;
import mineclone.client.renderer.model.IBlockModel;
import mineclone.client.renderer.world.BlockTextures;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.IBlockState;

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
