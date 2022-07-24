package mineclone.common.world.block;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.signal.SignalSource;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.state.IBlockState;

public class PoweredBlock extends Block implements SignalSource {

	private final SignalType type;

	public PoweredBlock(SignalType type) {
		this.type = type;
	}

	@Override
	public boolean isSolid() {
		return true;
	}

	@Override
	public SignalType getSignalType() {
		return type;
	}

	@Override
	public int getSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		return type.max();
	}

	@Override
	public int getDirectSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		return type.min();
	}

	@Override
	public boolean connectsToWire(IBlockState state, Direction dir) {
		return true;
	}
}
