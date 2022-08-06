package mineclone.common.world.block.signal.wire;

import mineclone.common.world.IWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.state.IBlockState;

public abstract class WireType {

	public static final WireType REDSTONE  = new BasicWireType(SignalType.REDSTONE);
	public static final WireType BLUESTONE = new BasicWireType(SignalType.BLUESTONE);

	protected final SignalType signal;
	protected final int min;
	protected final int max;
	protected final int step;

	protected WireType(SignalType signal, int min, int max, int step) {
		if (signal == null || signal.isAny()) {
			throw new IllegalArgumentException("signal type cannot be null or any");
		}
		if (max < min) {
			throw new IllegalArgumentException("max cannot be less than min");
		}
		if (min < signal.min() || min > signal.max()) {
			throw new IllegalArgumentException("min must be bound by signal type's min/max");
		}
		if (max < signal.min() || max > signal.max()) {
			throw new IllegalArgumentException("max must be bound by signal type's min/max");
		}
		if (step < 0) {
			throw new IllegalArgumentException("step must be at least 0");
		}

		this.signal = signal;
		this.min = min;
		this.max = max;
		this.step = step;
	}

	protected WireType(SignalType signal, int step) {
		this(signal, signal.min(), signal.max(), step);
	}

	protected WireType(SignalType signal) {
		this(signal, 1);
	}

	public final SignalType signal() {
		return signal;
	}

	public final int min() {
		return min;
	}

	public final int max() {
		return max;
	}

	public final int step() {
		return step;
	}

	public final int clamp(int signal) {
		return signal >= max ? max : (signal <= min ? min : signal);
	}

	/**
	 * Override only! Call {@link WireType#areCompatible WireType.areCompatible} instead.
	 */
	protected boolean canInteractWith(WireType type) {
		return signal.is(type.signal);
	}

	public static boolean areCompatible(WireType a, WireType b) {
		return a.canInteractWith(b) && b.canInteractWith(a);
	}

	public void findConnections(IWorld world, IBlockPosition pos, ConnectionConsumer consumer) {
		findPotentialConnections(world, pos, (side, neighborPos, neighborState, connection) -> {
			connection = validateConnection(world, pos, side, neighborPos, neighborState, connection);

			if (connection != ConnectionType.NONE) {
				boolean findNext = consumer.accept(side, neighborPos, neighborState, connection);

				if (!findNext) {
					return false;
				}
			}

			return true;
		});
	}

	public abstract void findPotentialConnections(IWorld world, IBlockPosition pos, PotentialConnectionConsumer consumer);

	public ConnectionType getConnection(IWorld world, IBlockPosition pos, ConnectionSide side) {
		ConnectionType connection = getPotentialConnection(world, pos, side);

		if (connection == ConnectionType.NONE) {
			return ConnectionType.NONE;
		}

		IBlockPosition neighborPos = side.offset(pos);
		IBlockState neighborState = world.getBlockState(neighborPos);

		return validateConnection(world, pos, side, neighborPos, neighborState, connection);
	}

	public abstract ConnectionType getPotentialConnection(IWorld world, IBlockPosition pos, ConnectionSide side);

	private ConnectionType validateConnection(IWorld world, IBlockPosition pos, ConnectionSide side, IBlockPosition neighborPos, IBlockState neighborState, ConnectionType connection) {
		if (connection == ConnectionType.NONE || !neighborState.isWire()) {
			return ConnectionType.NONE;
		}

		IWire neighborWire = (IWire)neighborState.getBlock();
		WireType neighborType = neighborWire.getWireType();

		if (this == neighborType) {
			return connection;
		}
		if (!areCompatible(this, neighborType)) {
			return ConnectionType.NONE;
		}

		return connection.and(neighborType.getPotentialConnection(world, neighborPos, side.getOpposite()));
	}

	@FunctionalInterface
	public interface ConnectionConsumer {

		/**
		 * @return whether to continue looking for connections
		 */
		boolean accept(ConnectionSide side, IBlockPosition neighborPos, IBlockState neighborState, ConnectionType connection);

	}

	@FunctionalInterface
	public interface PotentialConnectionConsumer {

		/**
		 * @return whether to continue looking for connections
		 */
		boolean accept(ConnectionSide side, IBlockPosition neighborPos, IBlockState neighborState, ConnectionType connection);

		/**
		 * @return whether to continue looking for connections
		 */
		default boolean accept(IWorld world, IBlockPosition pos, ConnectionSide side, ConnectionType connection) {
			IBlockPosition neighborPos = side.offset(pos);
			IBlockState neighborState = world.getBlockState(neighborPos);

			return accept(side, neighborPos, neighborState, connection);
		}
	}
}
