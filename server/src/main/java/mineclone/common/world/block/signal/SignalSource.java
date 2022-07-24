package mineclone.common.world.block.signal;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public interface SignalSource extends IBlock {

	@Override
	default boolean isSignalSource(IBlockState state, SignalType type) {
		return getSignalType().is(type);
	}

	@Override
	default int getSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, SignalType type) {
		return isSignalSource(state, type) ? getSignal(world, pos, state, dir) : type.min();
	}

	@Override
	default int getDirectSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, SignalType type) {
		return isSignalSource(state, type) ? getDirectSignal(world, pos, state, dir) : type.min();
	}

	SignalType getSignalType();

	int getSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir);

	int getDirectSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir);

}
