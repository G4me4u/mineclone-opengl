package minecraft.common.world.block.state;

import java.util.Arrays;

public final class EnumBlockProperty<T extends Enum<T> & IIndexedValue> implements IBlockProperty<T> {

	private final String name;
	private final T[] values;
	
	private final int hash;
	
	public EnumBlockProperty(String name, T[] values) {
		if (!isValidOrder(values))
			throw new IllegalArgumentException("Value order is not valid!");
		
		this.name = name;
		this.values = values;
	
		hash = name.hashCode() * 31 + Arrays.hashCode(values);
	}
	
	private static <T extends IIndexedValue> boolean isValidOrder(T[] values) {
		for (int i = 0; i < values.length; i++) {
			if (i != values[i].getIndex())
				return false;
		}
		
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getValueIndex(T value) {
		int index = value.getIndex();
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
		if (!Arrays.equals(values, property.values))
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "EnumBlockProperty[name=\"" + name + "\", values=" + Arrays.toString(values) + "]";
	}
}
