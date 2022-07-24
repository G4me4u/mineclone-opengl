package mineclone.common.world.block.signal.wire;

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
	default SignalType getConsumedSignalType() {
		return getWireType().signal();
	}

	WireType getWireType();

}
