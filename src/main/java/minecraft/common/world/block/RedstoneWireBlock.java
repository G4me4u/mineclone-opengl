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
	
	private WireConnection getWireConnection(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		IBlockPosition sidePos = pos.offset(dir);
		IBlockState sideState = world.getBlockState(sidePos);
		
		if (shouldConnectTo(sideState, sidePos, dir)) {
			return WireConnection.SIDE;
		} else if (sideState.getBlock().isSolid()) {
			IBlockPosition upPos = pos.up();
			
			if (!world.getBlock(upPos).isSolid() && world.getBlock(upPos) == Blocks.REDSTONE_WIRE_BLOCK)
				return WireConnection.UP;
		} else if (world.getBlock(sidePos.down()) == Blocks.REDSTONE_WIRE_BLOCK) {
			return WireConnection.SIDE;
		}
		
		return WireConnection.NONE;
	}
	
	private boolean shouldConnectTo(IBlockState state, IBlockPosition pos, Direction dir) {
		if (state.getBlock().isPowerComponent())
			return ((state.getOutputPowerFlags(dir) | state.getInputPowerFlags(dir)) != 0);
		return false;
	}

	@Override
	public void onBlockUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockState neighborState) {
	}
	
	@Override
	public void onStateUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockState neighborState) {
		if (dir.getAxis().isHorizontal()) {
			IBlockProperty<WireConnection> connectionProperty = CONNECTION_PROPERTIES.get(dir);
			WireConnection newConnection = getWireConnection(world, pos, state, dir);
			
			if (state.getValue(connectionProperty) != newConnection)
				world.setBlockState(pos, state.withProperty(connectionProperty, newConnection), true);
		}
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, POWER, NORTH_CONNECTION, SOUTH_CONNECTION, WEST_CONNECTION, EAST_CONNECTION);
	}
}
