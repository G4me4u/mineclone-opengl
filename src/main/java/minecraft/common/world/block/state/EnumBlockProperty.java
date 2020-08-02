package minecraft.common.world.block.state;

import java.util.Arrays;

public final class EnumBlockProperty<T extends Enum<T>> implements IBlockProperty<T> {

	private final String name;
	private final T[] values;
	
	private final int hash;
	
	public EnumBlockProperty(String name, T[] values) {
		this.name = name;
		this.values = values;
	
		hash = name.hashCode() * 31 + Arrays.hashCode(values);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getValueIndex(T value) {
		for (int i = 0; i < values.length; i++) {
			if (values[i] == value)
				return i;
		}
		
		throw new IllegalArgumentException("Invalid enum type! " + value);
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
