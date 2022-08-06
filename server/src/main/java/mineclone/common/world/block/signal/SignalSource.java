package mineclone.common.world.block.signal;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.signal.wire.ConnectionSide;
import mineclone.common.world.block.signal.wire.WireType;
import mineclone.common.world.block.state.IBlockState;

public interface SignalSource extends IBlock {

	@Override
	default boolean isSignalSource(SignalType type) {
		return getSignalType().is(type);
	}

	@Override
	default int getSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, SignalType type) {
		return isSignalSource(type) ? getSignal(world, pos, state, dir) : type.min();
	}

	@Override
	default int getDirectSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, SignalType type) {
		return isSignalSource(type) ? getDirectSignal(world, pos, state, dir) : type.min();
	}

	@Override
	default boolean shouldWireConnect(IWorld world, IBlockPosition pos, IBlockState state, ConnectionSide side, WireType type) {
		return isSignalSource(type.signal()) && shouldWireConnect(world, pos, state, side);
	}

	SignalType getSignalType();

	int getSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir);

	int getDirectSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir);

	boolean shouldWireConnect(IWorld world, IBlockPosition pos, IBlockState state, ConnectionSide side);

}
