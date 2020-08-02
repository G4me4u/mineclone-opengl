package minecraft.common.world.block;

import minecraft.client.renderer.model.BasicBlockModel;
import minecraft.client.renderer.model.IBlockModel;
import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.World;
import minecraft.common.world.block.state.BlockState;

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
