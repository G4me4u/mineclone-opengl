package mineclone.common.world.block;

import java.util.EnumMap;
import java.util.Map;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.signal.wire.ConnectionSide;
import mineclone.common.world.block.signal.wire.ConnectionType;
import mineclone.common.world.block.signal.wire.IWire;
import mineclone.common.world.block.signal.wire.WireType;
import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.block.state.IIndexedValue;
import mineclone.common.world.block.state.IntBlockProperty;
import mineclone.common.world.flags.SetBlockFlags;

public class BasicWireBlock extends Block implements IWire {

	public static final IBlockProperty<Integer> POWER = new IntBlockProperty("power", 16);

	public static final IBlockProperty<WireSide> NORTH_CONNECTION = new EnumBlockProperty<>("north", WireSide.ALL);
	public static final IBlockProperty<WireSide> SOUTH_CONNECTION = new EnumBlockProperty<>("south", WireSide.ALL);
	public static final IBlockProperty<WireSide> WEST_CONNECTION  = new EnumBlockProperty<>("west" , WireSide.ALL);
	public static final IBlockProperty<WireSide> EAST_CONNECTION  = new EnumBlockProperty<>("east" , WireSide.ALL);

	public static final Map<Direction, IBlockProperty<WireSide>> CONNECTIONS = new EnumMap<>(Direction.class);

	static {
		CONNECTIONS.put(Direction.NORTH, NORTH_CONNECTION);
		CONNECTIONS.put(Direction.SOUTH, SOUTH_CONNECTION);
		CONNECTIONS.put(Direction.WEST, WEST_CONNECTION);
		CONNECTIONS.put(Direction.EAST, EAST_CONNECTION);
	}

	private final WireType type;

	public BasicWireBlock(WireType type) {
		this.type = type;
	}

	@Override
	public IBlockState getPlacementState(IWorld world, IBlockPosition pos, IBlockState state) {
		if (!canExist(world, pos, state))
			return Blocks.AIR_BLOCK.getDefaultState();

		return updateAndResolveState(world, pos, state);
	}

	@Override
	public boolean canExist(IWorld world, IBlockPosition pos, IBlockState state) {
		return world.getBlockState(pos.down()).isAligned(Direction.UP);
	}

	@Override
	public void onAdded(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.getWireHandler().onWireAdded(pos);
	}

	@Override
	public void onRemoved(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.getWireHandler().onWireRemoved(pos, state);
	}

	@Override
	public void updateNeighbors(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.updateNeighbors(pos);

		for (Direction dir : Direction.ALL) {
			world.updateNeighborsExceptFrom(pos.offset(dir), dir.getOpposite());
		}
	}

	@Override
	public void updateNeighborShapes(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.updateNeighborShapes(pos, state);

		for (Direction ver : Direction.VERTICAL) {
			IBlockPosition verPos = pos.offset(ver);

			for (Direction hor : Direction.HORIZONTAL) {
				world.updateNeighborShape(verPos.offset(hor), hor.getOpposite(), pos, state);
			}
		}
	}

	@Override
	public void update(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.getWireHandler().onWireUpdate(pos);
	}

	@Override
	public void updateShape(IServerWorld world, IBlockPosition pos, IBlockState state, Direction neighborDir, IBlockPosition neighborPos, IBlockState neighborState) {
		IBlockState newState = state;

		if (neighborDir.isHorizontal()) {
			WireSide newConnection = findConnection(world, pos, neighborDir);
			IBlockProperty<WireSide> property = CONNECTIONS.get(neighborDir);

			if (newConnection != state.get(property) || newConnection == WireSide.SIDE) {
				newState = state.with(property, newConnection);

				// Update connections in the remaining horizontal directions.
				for (Direction dir = neighborDir; (dir = dir.rotateCCW()) != neighborDir; )
					newState = updateConnection(world, pos, newState, dir);

				newState = resolveState(newState);
			}
		} else if (!neighborState.isWire()) {
			if (neighborDir == Direction.DOWN) {
				// Update might come from the supporting block
				if (!canExist(world, pos, state))
					newState = Blocks.AIR_BLOCK.getDefaultState();
			} else {
				// The aligned faces of the top block might have changed.
				newState = updateAndResolveState(world, pos, state);
			}
		}

		if (state != newState)
			world.setBlockState(pos, newState, SetBlockFlags.UPDATE_NEIGHBOR_SHAPES | SetBlockFlags.UPDATE_NEIGHBORS);
	}

	private IBlockState updateAndResolveState(IWorld world, IBlockPosition pos, IBlockState state) {
		for (Direction dir : Direction.HORIZONTAL)
			state = updateConnection(world, pos, state, dir);

		return resolveState(state);
	}

	private IBlockState updateConnection(IWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		WireSide connection = findConnection(world, pos, dir);
		return state.with(CONNECTIONS.get(dir), connection);
	}

	private WireSide findConnection(IWorld world, IBlockPosition pos, Direction dir) {
		ConnectionSide side = ConnectionSide.fromDirection(dir);

		if (type.getConnection(world, pos, side.withUp()) != ConnectionType.NONE) {
			Direction opp = dir.getOpposite();
			IBlockPosition adjacent = pos.offset(dir);
			IBlockState adjacentState = world.getBlockState(adjacent);

			return adjacentState.isAligned(opp) ? WireSide.UP : WireSide.SIDE;
		}
		if (type.getConnection(world, pos, side) != ConnectionType.NONE) {
			return WireSide.SIDE;
		}
		if (type.getConnection(world, pos, side.withDown()) != ConnectionType.NONE) {
			return WireSide.SIDE;
		}

		return WireSide.NONE;
	}

	private IBlockState resolveState(IBlockState state) {
		Direction connectionDir = null;

		for (Direction dir : Direction.HORIZONTAL) {
			WireSide connection = state.get(CONNECTIONS.get(dir));

			if (connection != WireSide.NONE) {
				if (connectionDir != null) {
					// There is only something to resolve, if there is
					// exactly zero or one connection in the state.
					return state;
				}

				connectionDir = dir;
			}
		}

		if (connectionDir == null) {
			for (Direction dir : Direction.HORIZONTAL)
				state = state.with(CONNECTIONS.get(dir), WireSide.SIDE);
		} else {
			Direction dir = connectionDir.getOpposite();

			state = state.with(CONNECTIONS.get(dir), WireSide.SIDE);
		}

		return state;
	}

	@Override
	public WireType getWireType() {
		return type;
	}

	@Override
	public int getSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		if (dir == Direction.UP) {
			return type.min();
		} else if (dir.isHorizontal()) {
			WireSide connection = state.get(CONNECTIONS.get(dir));

			if (connection == WireSide.NONE) {
				return type.min();
			}
		}

		return getSignal(state);
	}

	@Override
	public int getDirectSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		return getSignal(world, pos, state, dir);
	}

	@Override
	public int getSignal(IBlockState state) {
		return state.get(POWER);
	}

	@Override
	public IBlockState setSignal(IBlockState state, int signal) {
		return state.with(POWER, signal);
	}

	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, POWER,
		                                        NORTH_CONNECTION,
		                                        SOUTH_CONNECTION,
		                                        WEST_CONNECTION,
		                                        EAST_CONNECTION);
	}

	public static WireSide getConnection(IBlockState state, Direction dir) {
		return dir.isVertical() ? WireSide.NONE : state.get(CONNECTIONS.get(dir));
	}

	public static boolean hasConnection(IBlockState state, Direction dir) {
		return getConnection(state, dir) != WireSide.NONE;
	}

	public static enum WireSide implements IIndexedValue {

		NONE("none", 0),
		SIDE("side", 1),
		UP  ("up"  , 2);

		public static final WireSide[] ALL;

		static {

			WireSide[] values = values();
			ALL = new WireSide[values.length];

			for (WireSide side : values) {
				ALL[side.index] = side;
			}
		}

		private final String name;
		private final int index;

		private WireSide(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public String getName() {
			return name;
		}

		@Override
		public int getIndex() {
			return index;
		}
	}
}
