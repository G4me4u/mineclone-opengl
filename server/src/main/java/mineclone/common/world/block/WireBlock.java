package mineclone.common.world.block;

import java.util.EnumMap;
import java.util.Map;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.signal.wire.Wire;
import mineclone.common.world.block.signal.wire.WireType;
import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.block.state.IntBlockProperty;
import mineclone.common.world.flags.SetBlockFlags;

public class WireBlock extends Block implements Wire {

	public static final IBlockProperty<Integer> POWER = new IntBlockProperty("power", 16);

	public static final IBlockProperty<WireConnection> NORTH_CONNECTION = new EnumBlockProperty<>("north", WireConnection.ALL);
	public static final IBlockProperty<WireConnection> SOUTH_CONNECTION = new EnumBlockProperty<>("south", WireConnection.ALL);
	public static final IBlockProperty<WireConnection> WEST_CONNECTION  = new EnumBlockProperty<>("west" , WireConnection.ALL);
	public static final IBlockProperty<WireConnection> EAST_CONNECTION  = new EnumBlockProperty<>("east" , WireConnection.ALL);

	public static final Map<Direction, IBlockProperty<WireConnection>> CONNECTIONS = new EnumMap<>(Direction.class);

	static {
		CONNECTIONS.put(Direction.NORTH, NORTH_CONNECTION);
		CONNECTIONS.put(Direction.SOUTH, SOUTH_CONNECTION);
		CONNECTIONS.put(Direction.WEST, WEST_CONNECTION);
		CONNECTIONS.put(Direction.EAST, EAST_CONNECTION);
	}

	private final WireType type;

	public WireBlock(WireType type) {
		this.type = type;
	}

	@Override
	public WireType getWireType() {
		return type;
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
	public IBlockState getPlacementState(IWorld world, IBlockPosition pos, IBlockState state) {
		if (!isWireSupported(world, pos))
			return Blocks.AIR_BLOCK.getDefaultState();

		return updateAndResolveState(world, pos, state);
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
		world.getWireHandler().onWireUpdated(pos);
	}

	@Override
	public void updateShape(IServerWorld world, IBlockPosition pos, IBlockState state, Direction neighborDir, IBlockPosition neighborPos, IBlockState neighborState) {
		IBlockState newState = state;

		if (neighborDir.isHorizontal()) {
			WireConnection newConnection = getWireConnection(world, pos, neighborDir);
			IBlockProperty<WireConnection> property = CONNECTIONS.get(neighborDir);

			if (newConnection != state.get(property) || newConnection == WireConnection.SIDE) {
				newState = state.with(property, newConnection);

				// Update connections in the remaining horizontal directions.
				for (Direction dir = neighborDir; (dir = dir.rotateCCW()) != neighborDir; )
					newState = updateStateConnectionTo(world, pos, newState, dir);

				newState = resolveState(newState);
			}
		} else if (!neighborState.isOf(Blocks.REDSTONE_WIRE_BLOCK)) {
			if (neighborDir == Direction.DOWN) {
				// Update might come from the supporting block
				if (!isWireSupported(world, pos))
					newState = Blocks.AIR_BLOCK.getDefaultState();
			} else {
				// The aligned faces of the top block might have changed.
				newState = updateAndResolveState(world, pos, state);
			}
		}

		if (state != newState)
			world.setBlockState(pos, newState, SetBlockFlags.UPDATE_NEIGHBOR_SHAPES | SetBlockFlags.UPDATE_NEIGHBORS);
	}

	private boolean isWireSupported(IWorld world, IBlockPosition pos) {
		return world.getBlockState(pos.down()).isAligned(Direction.UP);
	}

	private IBlockState updateAndResolveState(IWorld world, IBlockPosition pos, IBlockState state) {
		for (Direction dir : Direction.HORIZONTAL)
			state = updateStateConnectionTo(world, pos, state, dir);

		return resolveState(state);
	}

	private IBlockState updateStateConnectionTo(IWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		WireConnection connection = getWireConnection(world, pos, dir);
		return state.with(CONNECTIONS.get(dir), connection);
	}

	private WireConnection getWireConnection(IWorld world, IBlockPosition pos, Direction dir) {
		IBlockPosition sidePos = pos.offset(dir);
		IBlockState sideState = world.getBlockState(sidePos);

		if (sideState.isAligned(dir.getOpposite())) {
			IBlockState aboveState = world.getBlockState(pos.up());

			if (!aboveState.isAligned(Direction.DOWN) && !aboveState.isAligned(dir)) {
				IBlockState diagonalState = world.getBlockState(sidePos.up());

				if (diagonalState.isOf(Blocks.REDSTONE_WIRE_BLOCK))
					return WireConnection.UP;
			}
		} else if (!sideState.isAligned(Direction.DOWN)) {
			IBlockState diagonalState = world.getBlockState(sidePos.down());

			if (diagonalState.isOf(Blocks.REDSTONE_WIRE_BLOCK))
				return WireConnection.SIDE;
		}

		if (sideState.connectsToWire(dir.getOpposite(), type))
			return WireConnection.SIDE;

		return WireConnection.NONE;
	}

	private IBlockState resolveState(IBlockState state) {
		Direction connectionDir = null;

		for (Direction dir : Direction.HORIZONTAL) {
			WireConnection connection = state.get(CONNECTIONS.get(dir));

			if (connection != WireConnection.NONE) {
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
				state = state.with(CONNECTIONS.get(dir), WireConnection.SIDE);
		} else {
			Direction dir = connectionDir.getOpposite();

			state = state.with(CONNECTIONS.get(dir), WireConnection.SIDE);
		}

		return state;
	}

	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, POWER,
				                                NORTH_CONNECTION,
				                                SOUTH_CONNECTION,
				                                WEST_CONNECTION,
				                                EAST_CONNECTION);
	}
}
