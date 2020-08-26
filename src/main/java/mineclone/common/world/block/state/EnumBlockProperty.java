package mineclone.common.world.block.state;

import java.util.Arrays;

public final class EnumBlockProperty<T extends Enum<T> & IIndexedValue> implements IBlockProperty<T> {

	private final String name;
	private final T[] values;
	
	private final int indexOffset;
	
	private final int hash;
	
	public EnumBlockProperty(String name, T[] values) {
		int indexOffset = (values.length != 0) ? values[0].getIndex() : 0;
		
		if (!isValidOrder(values, indexOffset))
			throw new IllegalArgumentException("Value order is not valid!");
		
		this.name = name;
		this.values = values;
		
		this.indexOffset = indexOffset;
	
		hash = name.hashCode() * 31 + Arrays.hashCode(values);
	}
	
	private static <T extends IIndexedValue> boolean isValidOrder(T[] values, int indexOffset) {
		for (int i = 0; i < values.length; i++) {
			if (indexOffset != values[i].getIndex())
				return false;
			
			indexOffset++;
		}
		
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getValueIndex(T value) {
		int index = value.getIndex() - indexOffset;
		if (index < 0 || index >= values.length)
			throw new IllegalArgumentException(value + " is not part of property " + name);
		
		return index;
	}
	
	@Override
	public T getValue(int index) {
		return values[index];
	}

	@Override
	public int getValueCount() {
		return values.length;
	}

	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;

		if (!(other instanceof EnumBlockProperty))
			return false;
		
		EnumBlockProperty<?> property = (EnumBlockProperty<?>)other;
		
		if (!name.equals(property.name))
			return false;

		if (values != property.values) {
			if (values.length != property.values.length)
				return false;
		
			for (int i = 0; i < values.length; i++) {
				if (values[i] != property.values[i])
					return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return "EnumBlockProperty[name=\"" + name + "\", values=" + Arrays.toString(values) + "]";
	}
}
