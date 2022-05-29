package mineclone.common.world.block;

import mineclone.common.world.block.state.IBlockState;

public class DirtBlock extends BasicSolidBlock {
	
	@Override
	public boolean canGrowVegetation(IBlockState state) {
		return true;
	}
}
