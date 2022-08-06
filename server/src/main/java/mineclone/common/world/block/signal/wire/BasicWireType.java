package mineclone.common.world.block.signal.wire;

import mineclone.common.util.Lazy;
import mineclone.common.world.Direction;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.state.IBlockState;

public class BasicWireType extends WireType {

	public BasicWireType(SignalType signal, int min, int max, int step) {
		super(signal, min, max, step);
	}

	public BasicWireType(SignalType signal, int step) {
		super(signal, step);
	}

	public BasicWireType(SignalType signal) {
		super(signal);
	}

	@Override
	public void findPotentialConnections(IWorld world, IBlockPosition pos, PotentialConnectionConsumer consumer) {
		Lazy<IBlockState> belowState = new Lazy<>(() -> world.getBlockState(pos.down()));
		Lazy<IBlockState> aboveState = new Lazy<>(() -> world.getBlockState(pos.up()));

		Lazy<Boolean> aboveIsConductor = new Lazy<>(() -> aboveState.get().isSignalConductor(Direction.DOWN, signal));

		for (Direction dir : Direction.HORIZONTAL) {
			IBlockPosition adjacent = pos.offset(dir);
			IBlockState adjacentState = world.getBlockState(adjacent);

			if (adjacentState.isWire()) {
				consumer.accept(ConnectionSide.fromDirection(dir), adjacent, adjacentState, ConnectionType.BOTH);
			} else {
				boolean adjacentIsConductor = adjacentState.isSignalConductor(dir.getOpposite(), signal);

				if (!adjacentIsConductor && !adjacentState.isSignalConductor(Direction.DOWN, signal)) {
					boolean belowIsConductor = belowState.get().isSignalConductor(dir, signal);

					ConnectionSide side = ConnectionSide.fromDirection(dir).withDown();
					ConnectionType connection = belowIsConductor ? ConnectionType.BOTH : ConnectionType.IN;

					consumer.accept(world, pos, side, connection);
				}
				if (!aboveIsConductor.get() && !aboveState.get().isSignalConductor(dir, signal)) {
					ConnectionSide side = ConnectionSide.fromDirection(dir).withUp();
					ConnectionType connection = adjacentIsConductor ? ConnectionType.BOTH : ConnectionType.OUT;

					consumer.accept(world, pos, side, connection);
				}
			}
		}
	}

	@Override
	public ConnectionType getPotentialConnection(IWorld world, IBlockPosition pos, ConnectionSide side) {
		// no connections straight up or down
		if (side.isAlignedVertical()) {
			return ConnectionType.NONE;
		}
		// connections directly adjacent
		if (side.isAlignedHorizontal()) {
			return ConnectionType.BOTH;
		}

		ConnectionSide ver = side.projectVertical();

		// no connections diagonally horizontal
		if (ver == null) {
			return ConnectionType.NONE;
		}

		ConnectionSide hor = side.projectHorizontal();
		Direction dir = hor.getDirection();

		if (ver == ConnectionSide.DOWN) {
			IBlockPosition adjacent = pos.offset(dir);
			IBlockState adjacentState = world.getBlockState(adjacent);

			if (adjacentState.isAligned(Direction.DOWN) || adjacentState.isAligned(dir.getOpposite())) {
				return ConnectionType.NONE;
			}

			IBlockPosition below = pos.down();
			IBlockState belowState = world.getBlockState(below);

			return belowState.isSignalConductor(dir, signal) ? ConnectionType.BOTH : ConnectionType.IN;
		}
		if (ver == ConnectionSide.UP) {
			IBlockPosition above = pos.up();
			IBlockState aboveState = world.getBlockState(above);

			if (aboveState.isAligned(Direction.DOWN) || aboveState.isAligned(dir)) {
				return ConnectionType.NONE;
			}

			IBlockPosition adjacent = pos.offset(dir);
			IBlockState adjacentState = world.getBlockState(adjacent);

			return adjacentState.isSignalConductor(dir.getOpposite(), signal) ? ConnectionType.BOTH : ConnectionType.OUT;
		}

		return ConnectionType.NONE;
	}
}
