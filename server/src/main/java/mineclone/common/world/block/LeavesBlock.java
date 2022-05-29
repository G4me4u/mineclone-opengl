package mineclone.common.world.block;

import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.IBlockState;

public class LeavesBlock extends Block {

	@Override
	protected boolean hasEntityHitbox(IWorld world, IBlockPosition pos, IBlockState state) {
		return true;
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}
}
