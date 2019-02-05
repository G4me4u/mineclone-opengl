package com.g4mesoft.minecraft.world.block.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.g4mesoft.minecraft.world.block.Block;

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
	
	public Block getBlock() {
		return block;
	}
	
	private <T> PropertyInfo getPropertyInfo(IBlockProperty<T> property) {
		PropertyInfo propertyInfo = propertyLookup.get(property);
		if (propertyInfo == null)
			throw new IllegalArgumentException(property + " is not part of this BlockState!");
		
		return propertyInfo;
	}
	
	public <T> T getValue(IBlockProperty<T> property) {
		int index = getPropertyInfo(property).getIndex();
		return property.getPropertyValue(values[index]);
	}

	public <T> BlockState withProperty(IBlockProperty<T> property, T value) {
		PropertyInfo propertyInfo = getPropertyInfo(property);
		int diff = property.getPropertyValueIndex(value) - values[propertyInfo.getIndex()];
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
		int index = stateIndex + 1;
		if (index >= states.length)
			return states[0];
		return states[index];
	}

	public BlockState decrementState() {
		int index = stateIndex - 1;
		if (index < 0)
			return states[states.length - 1];
		return states[index];
	}
	
	public static BlockState createStateTree(Block block, IBlockProperty<?>[] properties) {
		int numProperties = properties.length;
		if (numProperties == 0)
			throw new IllegalArgumentException("Property arrays is of length 0");
			
		Map<IBlockProperty<?>, PropertyInfo> propertyLookup;
		propertyLookup = new HashMap<IBlockProperty<?>, PropertyInfo>(numProperties);
		
		// Make sure properties of the
		// same name don't exist.
		HashSet<String> nameTable = new HashSet<String>();
		
		int stride = 1;
		for (int i = numProperties - 1; i >= 0; i--) {
			IBlockProperty<?> property = properties[i];
			
			String name = property.getName();
			if (!nameTable.add(name))
				throw new IllegalArgumentException("Douplicate property of name \"" + name + "\"");
			
			PropertyInfo propertyInfo = new PropertyInfo(i, stride);
			propertyLookup.put(property, propertyInfo);

			int numValues = property.getNumValues();
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
				
				if (value >= property.getNumValues())
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
		for (Map.Entry<IBlockProperty<?>, PropertyInfo> entry : propertyLookup.entrySet()) {
			sb.append(',').append(' ');
			
			IBlockProperty<?> property = entry.getKey();
			PropertyInfo info = entry.getValue();
			sb.append(property.toString()).append('=');
			sb.append(property.getPropertyValue(values[info.index]));
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
