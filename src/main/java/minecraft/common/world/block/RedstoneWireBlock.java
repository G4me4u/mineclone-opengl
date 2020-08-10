package minecraft.common.world.block;

import java.util.EnumMap;
import java.util.Map;

import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.block.state.BlockState;
import minecraft.common.world.block.state.EnumBlockProperty;
import minecraft.common.world.block.state.IBlockProperty;
import minecraft.common.world.block.state.IBlockState;
import minecraft.common.world.block.state.IntBlockProperty;

public class RedstoneWireBlock extends Block {
	
	public static final IBlockProperty<Integer> POWER = new IntBlockProperty("power", 16);
	
	public static final IBlockProperty<WireConnection> NORTH_CONNECTION = new EnumBlockProperty<>("north", WireConnection.CONNECTIONS);
	public static final IBlockProperty<WireConnection> SOUTH_CONNECTION = new EnumBlockProperty<>("south", WireConnection.CONNECTIONS);
	public static final IBlockProperty<WireConnection> WEST_CONNECTION  = new EnumBlockProperty<>("west" , WireConnection.CONNECTIONS);
	public static final IBlockProperty<WireConnection> EAST_CONNECTION  = new EnumBlockProperty<>("east" , WireConnection.CONNECTIONS);
	
	public static final Map<Direction, IBlockProperty<WireConnection>> CONNECTION_PROPERTIES = new EnumMap<>(Direction.class);
	
	static {
		CONNECTION_PROPERTIES.put(Direction.NORTH, NORTH_CONNECTION);
		CONNECTION_PROPERTIES.put(Direction.SOUTH, SOUTH_CONNECTION);
		CONNECTION_PROPERTIES.put(Direction.WEST , WEST_CONNECTION);
		CONNECTION_PROPERTIES.put(Direction.EAST , EAST_CONNECTION);
	}
	
	public RedstoneWireBlock() {
	}
	
	@Override
	public IBlockState getPlacementState(IBlockState state, IServerWorld world, IBlockPosition pos) {
		if (!world.getBlockState(pos.down()).hasAlignedTop())
			return Blocks.AIR_BLOCK.getDefaultState();
		
		for (Direction dir : Direction.HORIZONTAL_DIRECTIONS)
			state = updateStateConnection(world, pos, state, dir);
		
		return resolveWireState(state);
	}

	@Override
	public void onStateReplaced(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.updateNeighbors(pos, IServerWorld.STATE_UPDATE_FLAG);
		
		for (Direction vertical : Direction.VERTICAL_DIRECTIONS) {
			IBlockPosition vpos = pos.offset(vertical);
			
			for (Direction horizontal : Direction.HORIZONTAL_DIRECTIONS)
				world.updateNeighbor(vpos.offset(horizontal), horizontal.getOpposite(), state, IServerWorld.STATE_UPDATE_FLAG);
		}
	}
	
	@Override
	public void onBlockUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
	}
	
	@Override
	public void onStateUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
		IBlockState newState = state;
		
		if (fromDir.getAxis().isHorizontal()) {
			if (fromState.isOf(Blocks.REDSTONE_WIRE_BLOCK)) {
				newState = updateStateConnection(world, pos, state, fromDir);
				
				if (newState != state) {
					// Update connections in the remaining horizontal directions.
					for (Direction dir = fromDir; (dir = dir.rotateCCW()) != fromDir; )
						newState = updateStateConnection(world, pos, newState, dir);

					newState = resolveWireState(newState);
				}
			}
		} else if (fromDir != Direction.UP || fromState.canPowerIndirectly()) {
			// Either the supporting block below has changed, or
			// the block above is obstructing connections.
			if (!fromState.isOf(Blocks.REDSTONE_WIRE_BLOCK))
				newState = getPlacementState(state, world, pos);
		}

		if (state != newState)
			world.setBlockState(pos, newState, true);
	}
	
	private IBlockState updateStateConnection(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		WireConnection connection = getWireConnection(world, pos, dir);
		return state.withProperty(CONNECTION_PROPERTIES.get(dir), connection);
	}
	
	private WireConnection getWireConnection(IServerWorld world, IBlockPosition pos, Direction dir) {
		IBlockPosition sidePos = pos.offset(dir);
		IBlockState sideState = world.getBlockState(sidePos);
		
		if (sideState.canPowerIndirectly()) {
			IBlockState aboveState = world.getBlockState(pos.up());
			
			if (!aboveState.canPowerIndirectly()) {
				IBlockState diagonalState = world.getBlockState(sidePos.up());
				
				if (diagonalState.isOf(Blocks.REDSTONE_WIRE_BLOCK))
					return WireConnection.UP;
			}
		} else if (world.getBlockState(sidePos.down()).isOf(Blocks.REDSTONE_WIRE_BLOCK)) {
			return WireConnection.SIDE;
		}
		
		if (sideState.canConnectToWire(dir.getOpposite()))
			return WireConnection.SIDE;
		
		return WireConnection.NONE;
	}
	
	private IBlockState resolveWireState(IBlockState state) {
		Direction connectionDir = null;
		
		for (Direction dir : Direction.HORIZONTAL_DIRECTIONS) {
			WireConnection connection = state.getValue(CONNECTION_PROPERTIES.get(dir));
			
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
			for (Direction dir : Direction.HORIZONTAL_DIRECTIONS)
				state = state.withProperty(CONNECTION_PROPERTIES.get(dir), WireConnection.SIDE);
		} else {
			Direction dir = connectionDir.getOpposite();
			
			state = state.withProperty(CONNECTION_PROPERTIES.get(dir), WireConnection.SIDE);
		}
		
		return state;
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, POWER, NORTH_CONNECTION, SOUTH_CONNECTION, WEST_CONNECTION, EAST_CONNECTION);
	}
}
