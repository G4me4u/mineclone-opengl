package minecraft.common.world.block.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;

public class BlockState {

	private final Block block;
	private final BlockState[] states;
	private final int stateIndex;
	private final Map<IBlockProperty<?>, PropertyInfo> propertyLookup;
	
	private final int[] values;
	
	private BlockState(Block block, BlockState[] states, int stateIndex, Map<IBlockProperty<?>, PropertyInfo> propertyLookup) {
		this.block = block;
		this.states = states;
		this.stateIndex = stateIndex;
		this.propertyLookup = propertyLookup;
		
		this.values = new int[propertyLookup.size()];
	}
	
	private BlockState(Block block) {
		this.block = block;

		states = null;
		stateIndex = 0;
		propertyLookup = null;
		
		values = null;
	}
	
	public boolean canGrowVegetation() {
		return block.canGrowVegetation(this);
	}

	public Block getBlock() {
		return block;
	}
	
	public void onBlockUpdate(IServerWorld world, IBlockPosition blockPos, Direction direction, BlockState sourceState) {
		block.onBlockUpdate(this, world, blockPos, direction, sourceState);
	}
	
	public void onStateUpdate(IServerWorld world, IBlockPosition blockPos, Direction direction, BlockState sourceState) {
		block.onStateUpdate(this, world, blockPos, direction, sourceState);
	}
	
	public void onInventoryUpdate(IServerWorld world, IBlockPosition blockPos, Direction direction, BlockState sourceState) {
		block.onInventoryUpdate(this, world, blockPos, direction, sourceState);
	}
	
	private <T> PropertyInfo getPropertyInfo(IBlockProperty<T> property) {
		if (propertyLookup == null)
			throw new IllegalStateException("BlockState has no properties!");

		PropertyInfo propertyInfo = propertyLookup.get(property);
		if (propertyInfo == null)
			throw new IllegalArgumentException(property + " is not part of this BlockState!");
		
		return propertyInfo;
	}
	
	public <T> T getValue(IBlockProperty<T> property) {
		int index = getPropertyInfo(property).getIndex();
		return property.getValue(values[index]);
	}

	public <T> BlockState withProperty(IBlockProperty<T> property, T value) {
		PropertyInfo propertyInfo = getPropertyInfo(property);
		int diff = property.getValueIndex(value) - values[propertyInfo.getIndex()];
		return states[stateIndex + diff * propertyInfo.stride];
	}
	
	public <T> BlockState incrementProperty(IBlockProperty<T> property) {
		int index = stateIndex + getPropertyInfo(property).stride;
		if (index >= states.length)
			index -= states.length;
		
		return states[index];
	}

	public <T> BlockState decrementProperty(IBlockProperty<T> property) {
		int index = stateIndex - getPropertyInfo(property).stride;
		if (index < 0)
			index += states.length;
		
		return states[index];
	}
	
	public BlockState incrementState() {
		// Just like the index would wrap around to zero when going above 
		// states.length - 1, we wrap around and return ourselves, if the
		// state array is empty.
		if (states == null)
			return this;

		int index = stateIndex + 1;
		if (index >= states.length)
			return states[0];
		return states[index];
	}

	public BlockState decrementState() {
		if (states == null)
			return this;
		
		int index = stateIndex - 1;
		if (index < 0)
			return states[states.length - 1];
		return states[index];
	}
	
	public static BlockState createStateTree(Block block, IBlockProperty<?>... properties) {
		int numProperties = properties.length;
		if (numProperties == 0)
			return new BlockState(block);

		Map<IBlockProperty<?>, PropertyInfo> propertyLookup = new HashMap<>(numProperties);

		// Make sure there are no properties with duplicates names.
		HashSet<String> nameTable = new HashSet<>();
		
		int stride = 1;
		for (int i = numProperties - 1; i >= 0; i--) {
			IBlockProperty<?> property = properties[i];
			
			String name = property.getName();
			if (!nameTable.add(name))
				throw new IllegalArgumentException("Duplicate property of name \"" + name + "\"");
			
			PropertyInfo propertyInfo = new PropertyInfo(i, stride);
			propertyLookup.put(property, propertyInfo);

			int numValues = property.getValueCount();
			if (numValues == 0)
				throw new IllegalArgumentException("Illegal property " + property);
			
			stride *= numValues;
		}
		
		// Clean up memory
		nameTable.clear();
		nameTable = null;
			
		BlockState[] states = new BlockState[stride];
		for (int i = 0; i < states.length; i++)
			states[i] = new BlockState(block, states, i, propertyLookup);
		
		// Populate tree
		for (int i = 0; i < numProperties; i++) {
			IBlockProperty<?> property = properties[i];
			PropertyInfo propertyInfo = propertyLookup.get(property);
			
			int index = 0;
			int value = 0;
			while (index < states.length) {
				for (int j = 0; j < propertyInfo.getStride(); j++) {
					BlockState state = states[index++];
					state.values[i] = value;
				}
				
				value++;
				
				if (value >= property.getValueCount())
					value = 0;
			}
		}
		
		return states[0];
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("BlockState[block=");
		if (block == null) {
			sb.append("null");
		} else {
			sb.append(block.getName());
		}

		if (propertyLookup != null) {
			for (Map.Entry<IBlockProperty<?>, PropertyInfo> entry : propertyLookup.entrySet()) {
				sb.append(',').append(' ');
				
				IBlockProperty<?> property = entry.getKey();
				PropertyInfo info = entry.getValue();
				sb.append(property.toString()).append('=');
				sb.append(property.getValue(values[info.index]));
			}
		}
		
		sb.append(']');

		return sb.toString();
	}

	private static class PropertyInfo {
		
		private final int index;
		private final int stride;
	
		public PropertyInfo(int index, int stride) {
			this.index = index;
			this.stride = stride;
		}
		
		public int getIndex() {
			return index;
		}
		
		public int getStride() {
			return stride;
		}
	}
}
