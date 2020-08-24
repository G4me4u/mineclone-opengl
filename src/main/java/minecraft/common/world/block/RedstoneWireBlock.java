package minecraft.common.world.block;

import java.util.EnumMap;
import java.util.Map;

import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.block.handler.WireHandler;
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
	
	public static final Map<Direction, IBlockProperty<WireConnection>> CONNECTIONS = new EnumMap<>(Direction.class);
	
	static {
		CONNECTIONS.put(Direction.NORTH, NORTH_CONNECTION);
		CONNECTIONS.put(Direction.SOUTH, SOUTH_CONNECTION);
		CONNECTIONS.put(Direction.WEST , WEST_CONNECTION);
		CONNECTIONS.put(Direction.EAST , EAST_CONNECTION);
	}
	
	public RedstoneWireBlock() {
	}
	
	@Override
	public IBlockState getPlacementState(IServerWorld world, IBlockPosition pos, IBlockState state) {
		if (!isWireSupported(world, pos))
			return Blocks.AIR_BLOCK.getDefaultState();
		
		for (Direction dir : Direction.HORIZONTAL)
			state = updateStateConnection(world, pos, state, dir);
		
		return resolveWireState(state);
	}
	
	@Override
	public void onBlockAdded(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.updateNeighbors(pos, IServerWorld.STATE_UPDATE_FLAG);
		
		for (Direction vertical : Direction.VERTICAL) {
			IBlockPosition vpos = pos.offset(vertical);
			
			for (Direction horizontal : Direction.HORIZONTAL)
				world.updateNeighbor(vpos.offset(horizontal), horizontal.getOpposite(), state, IServerWorld.STATE_UPDATE_FLAG);
		}
	}
	
	@Override
	public void onBlockRemoved(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.updateNeighbors(pos, IServerWorld.STATE_UPDATE_FLAG);
		
		for (Direction vertical : Direction.VERTICAL) {
			IBlockPosition vpos = pos.offset(vertical);
			
			for (Direction horizontal : Direction.HORIZONTAL)
				world.updateNeighbor(vpos.offset(horizontal), horizontal.getOpposite(), state, IServerWorld.STATE_UPDATE_FLAG);
		}
	}
	
	@Override
	public void onStateChanged(IServerWorld world, IBlockPosition pos, IBlockState newState, IBlockState oldState) {
		world.updateNeighbors(pos, IServerWorld.STATE_UPDATE_FLAG);
	}
	
	@Override
	public void onBlockUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
		int power = state.get(POWER);
		int sourcePower = world.getPowerFrom(pos, fromDir, IServerWorld.STRONG_POWER_FLAGS);
		
		if (power != sourcePower) {
			WireHandler wireHandler;
			if (sourcePower > power) {
				//wireHandler = new WireHandler(world, state, pos, power, sourcePower);
			} else {
				int otherPower = world.getPowerExceptFrom(pos, fromDir, IServerWorld.STRONG_POWER_FLAGS);
				int powerReceived = sourcePower > otherPower ? sourcePower : otherPower;
				
				if (powerReceived == power) {
					return;
				}
				
				//wireHandler = new WireHandler(world, state, pos, power, powerReceived);
			}
			
			//wireHandler.setPowerLevels();
		}
	}
	
	@Override
	public void onStateUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
		IBlockState newState = state;
		
		if (fromDir.isHorizontal()) {
			if (fromState.isOf(Blocks.REDSTONE_WIRE_BLOCK)) {
				newState = updateStateConnection(world, pos, state, fromDir);
				
				if (newState != state) {
					// Update connections in the remaining horizontal directions.
					for (Direction dir = fromDir; (dir = dir.rotateCCW()) != fromDir; )
						newState = updateStateConnection(world, pos, newState, dir);

					newState = resolveWireState(newState);
				}
			}
		} else if (!fromState.isOf(Blocks.REDSTONE_WIRE_BLOCK)) {
			if (fromDir == Direction.DOWN) {
				// Update might come from the supporting block
				if (!isWireSupported(world, pos))
					newState = Blocks.AIR_BLOCK.getDefaultState();
			} else {
				// The aligned faces of the top block might have changed.
				// Make sure we update the appropriate directions.
				for (Direction dir : Direction.HORIZONTAL) {
					if (newState.get(CONNECTIONS.get(dir)) != WireConnection.SIDE)
						newState = updateStateConnection(world, pos, newState, dir);
				}
				
				newState = resolveWireState(newState);
			}
		}

		if (state != newState)
			world.setBlockState(pos, newState, true);
	}
	
	private boolean isWireSupported(IServerWorld world, IBlockPosition pos) {
		return world.getBlockState(pos.down()).isAligned(Direction.UP);
	}
	
	private IBlockState updateStateConnection(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		WireConnection connection = getWireConnection(world, pos, dir);
		return state.with(CONNECTIONS.get(dir), connection);
	}
	
	private WireConnection getWireConnection(IServerWorld world, IBlockPosition pos, Direction dir) {
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
		
		if (sideState.canConnectToWire(dir.getOpposite()))
			return WireConnection.SIDE;
		
		return WireConnection.NONE;
	}
	
	private IBlockState resolveWireState(IBlockState state) {
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
