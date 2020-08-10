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
		for (Direction dir : Direction.HORIZONTAL_DIRECTIONS) {
			WireConnection connection = getWireConnection(world, pos, dir);
			state = state.withProperty(CONNECTION_PROPERTIES.get(dir), connection);
		}
		
		return resolveWireState(state);
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
			state = state.withProperty(NORTH_CONNECTION, WireConnection.SIDE);
			state = state.withProperty(SOUTH_CONNECTION, WireConnection.SIDE);
			state = state.withProperty(WEST_CONNECTION , WireConnection.SIDE);
			state = state.withProperty(EAST_CONNECTION , WireConnection.SIDE);
		} else {
			Direction dir = connectionDir.getOpposite();
			
			state = state.withProperty(CONNECTION_PROPERTIES.get(dir), WireConnection.SIDE);
		}
		
		return state;
	}
	
	@Override
	public void onBlockUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
	}
	
	@Override
	public void onStateUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
		if (fromDir.getAxis().isHorizontal() && fromState.isOf(Blocks.REDSTONE_WIRE_BLOCK)) {
			IBlockProperty<WireConnection> property = CONNECTION_PROPERTIES.get(fromDir);
			WireConnection newConnection = getWireConnection(world, pos, fromDir);
			
			if (newConnection != state.getValue(property)) {
				state = state.withProperty(property, newConnection);
				
				Direction dir = fromDir.rotateCCW();
				
				do {
					WireConnection connection = getWireConnection(world, pos, dir);
					state = state.withProperty(CONNECTION_PROPERTIES.get(dir), connection);
				} while ((dir = dir.rotateCCW()) != fromDir);
				
				world.setBlockState(pos, resolveWireState(state), true);
			}
		}
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, POWER, NORTH_CONNECTION, SOUTH_CONNECTION, WEST_CONNECTION, EAST_CONNECTION);
	}
}
