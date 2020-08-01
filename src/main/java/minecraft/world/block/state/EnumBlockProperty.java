package minecraft.world.block.state;

import java.util.Arrays;

public class EnumBlockProperty<T extends Enum<T>> implements IBlockProperty<T> {

	private final String propertyName;
	private final T[] values;
	
	private final int hash;
	
	public EnumBlockProperty(String propertyName, T[] values) {
		this.propertyName = propertyName;
		this.values = values;
	
		hash = propertyName.hashCode() * 31 + Arrays.hashCode(values);
	}
	
	@Override
	public int getPropertyValueIndex(T value) {
		for (int i = 0; i < values.length; i++) {
			if (values[i] == value)
				return i;
		}
		
		throw new IllegalArgumentException("Invalid enum type! " + value);
	}

	@Override
	public T getPropertyValue(int index) {
		return values[index];
	}

	@Override
	public String getName() {
		return propertyName;
	}

	@Override
	public int getNumValues() {
		return values.length;
	}

	public boolean equals(EnumBlockProperty<?> other) {
		if (other == this)
			return true;
		
		if (other != null && propertyName.equals(other.propertyName))
			return Arrays.equals(values, other.values);
		return false;
	}
	
	@Override
	public boolean equals(IBlockProperty<T> other) {
		if (!(other instanceof EnumBlockProperty))
			return false;
		
		return equals((EnumBlockProperty<?>)other);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof EnumBlockProperty))
			return false;

		return equals((EnumBlockProperty<?>)other);
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public String toString() {
		return "EnumBlockProperty[name=\"" + propertyName + "\", values=" + Arrays.toString(values) + "]";
	}
}
