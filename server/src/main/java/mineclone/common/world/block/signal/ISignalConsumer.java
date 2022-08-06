package mineclone.common.world.block.signal;

import mineclone.common.world.block.IBlock;

public interface ISignalConsumer extends IBlock {

	@Override
	default boolean isSignalConsumer(SignalType type) {
		return getConsumedSignalType().is(type);
	}

	SignalType getConsumedSignalType();

}
