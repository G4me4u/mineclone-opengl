package mineclone.common.world.block.signal.wire;

import mineclone.common.world.IWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.signal.SignalConsumer;
import mineclone.common.world.block.signal.SignalSource;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.state.IBlockState;

public interface Wire extends SignalSource, SignalConsumer {

	@Override
	default boolean isWire() {
		return true;
	}

	@Override
	default boolean isWire(SignalType type) {
		return isSignalSource(type);
	}

	@Override
	default boolean isWire(WireType type) {
		return getWireType() == type;
	}

	@Override
	default boolean shouldWireConnect(IWorld world, IBlockPosition pos, IBlockState state, ConnectionSide side, WireType neighborType) {
		WireType type = getWireType();

		if (!WireType.areCompatible(type, neighborType)) {
			return false;
		}

		ConnectionType connection = type.getPotentialConnection(world, pos, side);

		if (type != neighborType) {
			IBlockPosition neighborPos = side.offset(pos);
			ConnectionSide neighborSide = side.getOpposite();

			connection = connection.and(neighborType.getPotentialConnection(world, neighborPos, neighborSide));
		}

		return connection != ConnectionType.NONE;
	}

	@Override
	default SignalType getSignalType() {
		return getWireType().signal();
	}

	@Override
	default boolean shouldWireConnect(IWorld world, IBlockPosition pos, IBlockState state, ConnectionSide side) {
		throw new UnsupportedOperationException();
	}

	@Override
	default SignalType getConsumedSignalType() {
		return getWireType().signal();
	}

	WireType getWireType();

	int getSignal(IBlockState state);

	IBlockState setSignal(IBlockState state, int signal);

}
