package mineclone.common.world.block.signal.wire;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.signal.SignalConsumer;
import mineclone.common.world.block.signal.SignalSource;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.state.IBlockState;

public interface Wire extends SignalSource, SignalConsumer {

	@Override
	default boolean isWire(IBlockState state) {
		return true;
	}

	@Override
	default boolean isWire(IBlockState state, WireType type) {
		return getWireType() == type;
	}

	@Override
	default SignalType getSignalType() {
		return getWireType().signal();
	}

	@Override
	default int getSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		return connectsToWire(state, dir) ? getSignal(state) : getWireType().min();
	}

	@Override
	default int getDirectSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		return connectsToWire(state, dir) ? getSignal(state) : getWireType().min();
	}

	@Override
	default boolean connectsToWire(IBlockState state, Direction dir) {
		return dir.isHorizontal();
	}

	@Override
	default SignalType getConsumedSignalType() {
		return getWireType().signal();
	}

	WireType getWireType();

	int getSignal(IBlockState state);

	IBlockState setSignal(IBlockState state, int signal);

}
