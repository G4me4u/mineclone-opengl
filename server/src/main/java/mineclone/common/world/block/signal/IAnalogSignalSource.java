package mineclone.common.world.block.signal;

import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public interface IAnalogSignalSource extends IBlock {

	@Override
	default boolean isAnalogSignalSource(SignalType type) {
		return getAnalogSignalType().is(type);
	}

	@Override
	default int getAnalogSignal(IServerWorld world, IBlockPosition pos, IBlockState state, SignalType type) {
		return isAnalogSignalSource(type) ? getAnalogSignal(world, pos, state, type.min(), type.max()) : type.min();
	}

	SignalType getAnalogSignalType();

	int getAnalogSignal(IServerWorld world, IBlockPosition pos, IBlockState state, int min, int max);

}
