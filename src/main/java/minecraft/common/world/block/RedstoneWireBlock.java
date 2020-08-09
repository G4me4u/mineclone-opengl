package minecraft.common.world.block;

import java.util.EnumMap;
import java.util.Map;

import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.block.state.BlockState;
import minecraft.common.world.block.state.EnumBlockProperty;
import minecraft.common.world.block.state.IBlockProperty;
import minecraft.common.world.block.state.IntBlockProperty;

public class RedstoneWireBlock extends Block {
	
	public static final IBlockProperty<Integer> POWER_PROPERTY = new IntBlockProperty("power", 16);
	public static final IBlockProperty<WireConnection> WIRE_CONNECTION_NORTH = new EnumBlockProperty<>("north", WireConnection.values());
	public static final IBlockProperty<WireConnection> WIRE_CONNECTION_SOUTH = new EnumBlockProperty<>("south", WireConnection.values());
	public static final IBlockProperty<WireConnection> WIRE_CONNECTION_WEST  = new EnumBlockProperty<>("west",  WireConnection.values());
	public static final IBlockProperty<WireConnection> WIRE_CONNECTION_EAST  = new EnumBlockProperty<>("east",  WireConnection.values());
	
	public static final Map<Direction, IBlockProperty<WireConnection>> WIRE_CONNECTIONS = new EnumMap<>(Direction.class);
	
	static {
		WIRE_CONNECTIONS.put(Direction.NORTH, WIRE_CONNECTION_NORTH);
		WIRE_CONNECTIONS.put(Direction.SOUTH, WIRE_CONNECTION_SOUTH);
		WIRE_CONNECTIONS.put(Direction.WEST, WIRE_CONNECTION_WEST);
		WIRE_CONNECTIONS.put(Direction.EAST, WIRE_CONNECTION_EAST);
	}
	
	public RedstoneWireBlock() {
		
	}
	
	private WireConnection getWireConnection(BlockState state, IServerWorld world, IBlockPosition blockPos, Direction direction) {
		IBlockPosition side = blockPos.getOffset(direction);
		BlockState sideState = world.getBlockState(side);
		if (sideState.connectsToRedstoneWire(direction.getOpposite())) {
			return WireConnection.SIDE;
		} else if (sideState.conductsRedstonePower()) {
			IBlockPosition up = blockPos.up();
			if (!world.getBlockState(up).conductsRedstonePower() && world.getBlockState(up.getOffset(direction)).isOf(Blocks.REDSTONE_WIRE_BLOCK)) {
				return WireConnection.UP;
			}
		} else if (world.getBlockState(blockPos.down().getOffset(direction)).isOf(Blocks.REDSTONE_WIRE_BLOCK)) {
			return WireConnection.SIDE;
		}
		return WireConnection.NONE;
	}
	
	@Override
	protected BlockState createDefaultState() {
		return BlockState.createStateTree(this, POWER_PROPERTY,
		                                        WIRE_CONNECTION_NORTH,
		                                        WIRE_CONNECTION_SOUTH,
		                                        WIRE_CONNECTION_WEST,
		                                        WIRE_CONNECTION_EAST);
	}
	
	@Override
	public void onBlockUpdate(BlockState state, IServerWorld world, IBlockPosition blockPos, Direction direction, BlockState neighborState) {
		
	}
	
	@Override
	public void onStateUpdate(BlockState state, IServerWorld world, IBlockPosition blockPos, Direction direction, BlockState neighborState) {
		if (direction.getAxis().isHorizontal()) {
			IBlockProperty<WireConnection> wireConnectionProperty = WIRE_CONNECTIONS.get(direction);
			WireConnection newWireConnection = getWireConnection(state, world, blockPos, direction);
			
			if (!state.getValue(wireConnectionProperty).equals(newWireConnection)) {
				world.setBlockState(blockPos, state.withProperty(wireConnectionProperty, newWireConnection), true);
			}
		}
	}
}
