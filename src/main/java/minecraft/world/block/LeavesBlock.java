package minecraft.world.block;

import minecraft.renderer.model.BasicBlockModel;
import minecraft.renderer.model.IBlockModel;
import minecraft.renderer.world.BlockTextures;
import minecraft.world.World;
import minecraft.world.block.state.BlockState;

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
