package mineclone.common.world.block;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.state.IBlockState;

public class RedstoneBlock extends Block {
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	public boolean canPowerIndirectly(IBlockState state, Direction dir) {
		return false;
	}
	
	@Override
	public boolean canConnectToWire(IBlockState state, Direction dir) {
		return true;
	}
	
	@Override
	public int getOutputPowerFlags(IBlockState state, Direction dir) {
		return IServerWorld.INDIRECT_STRONG_POWER_FLAG;
	}
	
	@Override
	public int getPowerTo(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, int powerFlags) {
		return (powerFlags & getOutputPowerFlags(state, dir)) == 0 ? 0 : 15;
	}
}
