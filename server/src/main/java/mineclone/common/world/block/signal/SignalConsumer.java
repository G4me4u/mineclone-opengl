package mineclone.common.world.block.signal;

import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.state.IBlockState;

public interface SignalConsumer extends IBlock {

	@Override
	default boolean isSignalConsumer(IBlockState state, SignalType type) {
		return getConsumedSignalType().is(type);
	}

	SignalType getConsumedSignalType();

}
