package mineclone.common.world.block;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.signal.ISignalSource;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.signal.wire.ConnectionSide;
import mineclone.common.world.block.state.IBlockState;

public class PoweredBlock extends Block implements ISignalSource {

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
	public boolean shouldWireConnect(IWorld world, IBlockPosition pos, IBlockState state, ConnectionSide side) {
		return side.isAligned();
	}
}
