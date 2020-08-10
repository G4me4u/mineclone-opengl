package minecraft.common.world.block;

import java.util.EnumMap;
import java.util.Map;

import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.block.handler.RedstoneWireHandler;
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
	public void onStateReplaced(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.updateNeighbors(pos, state, IServerWorld.STATE_UPDATE_FLAG);
		
		for (Direction vertical : Direction.VERTICAL_DIRECTIONS) {
			for (Direction horizontal : Direction.HORIZONTAL_DIRECTIONS) {
				IBlockPosition neighborPos = pos.offset(vertical).offset(horizontal);
				world.updateNeighbor(neighborPos, horizontal.getOpposite(), state, IServerWorld.STATE_UPDATE_FLAG);
			}
		}
	}
	
	private WireConnection getWireConnection(IServerWorld world, IBlockPosition pos, Direction dir) {
		IBlockPosition sidePos = pos.offset(dir);
		IBlockState sideState = world.getBlockState(sidePos);
		
		if (sideState.conductsPower()) {
			IBlockPosition upPos = pos.up();
			IBlockPosition sideUpPos = sidePos.up();
			
			if (!world.getBlockState(upPos).conductsPower() && world.getBlock(sideUpPos) == Blocks.REDSTONE_WIRE_BLOCK) {
				return WireConnection.UP;
			}
		} else if (world.getBlock(sidePos.down()) == Blocks.REDSTONE_WIRE_BLOCK) {
			return WireConnection.SIDE;
		}
		if (sideState.connectsToWire(dir.getOpposite())) {
			return WireConnection.SIDE;
		}
		return WireConnection.NONE;
	}

	@Override
	public void onBlockUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockState neighborState) {
		int power = state.getValue(POWER);
		int sourcePower = world.getPowerFrom(pos, dir, IServerWorld.STRONG_POWER_FLAGS);
		
		if (power != sourcePower) {
			RedstoneWireHandler wireHandler;
			if (sourcePower > power) {
				wireHandler = new RedstoneWireHandler(world, state, pos, power, sourcePower);
			} else {
				int otherPower = world.getPowerExceptFrom(pos, dir, IServerWorld.STRONG_POWER_FLAGS);
				int powerReceived = sourcePower > otherPower ? sourcePower : otherPower;
				
				if (powerReceived == power) {
					return;
				}
				
				wireHandler = new RedstoneWireHandler(world, state, pos, power, powerReceived);
			}
			
			wireHandler.setPowerLevels();
		}
	}
	
	@Override
	public void onStateUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockState neighborState) {
		if (dir.getAxis().isHorizontal()) {
			IBlockProperty<WireConnection> connectionProperty = CONNECTION_PROPERTIES.get(dir);
			WireConnection newConnection = getWireConnection(world, pos, dir);
			
			if (state.getValue(connectionProperty) != newConnection) {
				IBlockState newState = state.withProperty(connectionProperty, newConnection);
				
				WireConnection sideConnectionCW = state.getValue(CONNECTION_PROPERTIES.get(dir.rotateCW()));
				WireConnection sideConnectionCCW = state.getValue(CONNECTION_PROPERTIES.get(dir.rotateCCW()));
				
				if (sideConnectionCW == WireConnection.NONE && sideConnectionCCW == WireConnection.NONE) {
					newConnection = newConnection == WireConnection.UP ? WireConnection.SIDE : newConnection;
					newState = newState.withProperty(CONNECTION_PROPERTIES.get(dir.getOpposite()), newConnection);
				}
				
				world.setBlockState(pos, newState, true);
			}
		}
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, POWER, NORTH_CONNECTION, SOUTH_CONNECTION, WEST_CONNECTION, EAST_CONNECTION);
	}
	
	@Override
	public IBlockState getPlacementState(IBlockState state, IServerWorld world, IBlockPosition pos) {
		Direction currentDir = null;
		int connectionCount = 0;
		for (Direction dir : Direction.HORIZONTAL_DIRECTIONS) {
			WireConnection connection = getWireConnection(world, pos, dir);
			
			if (connection != WireConnection.NONE) {
				connectionCount++;
				currentDir = dir;
				IBlockProperty<WireConnection> connectionProperty = CONNECTION_PROPERTIES.get(dir);
				state = state.withProperty(connectionProperty, connection);
			}
		}
		if (connectionCount == 1) {
			state = state.withProperty(CONNECTION_PROPERTIES.get(currentDir.getOpposite()), WireConnection.SIDE);
		}
		
		return state;
	}
}
