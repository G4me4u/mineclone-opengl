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
	
	public static final IBlockProperty<Integer> POWER_PROPERTY = new IntBlockProperty("power", 16);
	public static final IBlockProperty<WireConnection> WIRE_CONNECTION_NORTH = new EnumBlockProperty<>("north", WireConnection.CONNECTIONS);
	public static final IBlockProperty<WireConnection> WIRE_CONNECTION_SOUTH = new EnumBlockProperty<>("south", WireConnection.CONNECTIONS);
	public static final IBlockProperty<WireConnection> WIRE_CONNECTION_WEST  = new EnumBlockProperty<>("west" , WireConnection.CONNECTIONS);
	public static final IBlockProperty<WireConnection> WIRE_CONNECTION_EAST  = new EnumBlockProperty<>("east" , WireConnection.CONNECTIONS);
	
	public static final Map<Direction, IBlockProperty<WireConnection>> WIRE_CONNECTIONS = new EnumMap<>(Direction.class);
	
	static {
		WIRE_CONNECTIONS.put(Direction.NORTH, WIRE_CONNECTION_NORTH);
		WIRE_CONNECTIONS.put(Direction.SOUTH, WIRE_CONNECTION_SOUTH);
		WIRE_CONNECTIONS.put(Direction.WEST , WIRE_CONNECTION_WEST);
		WIRE_CONNECTIONS.put(Direction.EAST , WIRE_CONNECTION_EAST);
	}
	
	public RedstoneWireBlock() {
	}
	
	private WireConnection getWireConnection(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		IBlockPosition side = pos.getOffset(dir);
		IBlockState sideState = world.getBlockState(side);
		
		if (sideState.connectsToRedstoneWire(dir.getOpposite())) {
			return WireConnection.SIDE;
		} else if (sideState.conductsRedstonePower()) {
			IBlockPosition up = pos.up();
			if (!world.getBlockState(up).conductsRedstonePower() && world.getBlockState(up.getOffset(dir)).isOf(Blocks.REDSTONE_WIRE_BLOCK)) {
				return WireConnection.UP;
			}
		} else if (world.getBlockState(pos.down().getOffset(dir)).isOf(Blocks.REDSTONE_WIRE_BLOCK)) {
			return WireConnection.SIDE;
		}
		
		return WireConnection.NONE;
	}
	
	@Override
	public void onBlockUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockState neighborState) {
	}
	
	@Override
	public void onStateUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockState neighborState) {
		if (dir.getAxis().isHorizontal()) {
			IBlockProperty<WireConnection> wireConnectionProperty = WIRE_CONNECTIONS.get(dir);
			WireConnection newWireConnection = getWireConnection(world, pos, state, dir);
			
			if (!state.getValue(wireConnectionProperty).equals(newWireConnection)) {
				world.setBlockState(pos, state.withProperty(wireConnectionProperty, newWireConnection), true);
			}
		}
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, POWER_PROPERTY,
		                                        WIRE_CONNECTION_NORTH,
		                                        WIRE_CONNECTION_SOUTH,
		                                        WIRE_CONNECTION_WEST,
		                                        WIRE_CONNECTION_EAST);
	}
}
