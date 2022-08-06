package mineclone.common.world.block;

import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.state.IBlockState;

public class AirBlock extends Block {

	@Override
	public boolean isAir() {
		return true;
	}

	@Override
	public void updateNeighbors(IServerWorld world, IBlockPosition pos, IBlockState state) {
	}

	@Override
	public void updateNeighborShapes(IServerWorld world, IBlockPosition pos, IBlockState state) {
	}
}
