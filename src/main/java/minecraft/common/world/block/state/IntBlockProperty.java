package minecraft.common.world.block.state;

public class IntBlockProperty implements IBlockProperty<Integer> {

	private final String propertyName;
	
	private final int min;
	private final int max;
	
	private final int hash;

	public IntBlockProperty(String propertyName, int max) {
		this(propertyName, 0, max);
	}
	
	public IntBlockProperty(String propertyName, int min, int max) {
		this.propertyName = propertyName;
		
		this.min = min;
		this.max = max;
	
		hash = propertyName.hashCode() * 31 * 31 + min * 31 + max;
	}

	@Override
	public int getPropertyValueIndex(Integer value) {
		int v = value.intValue();
		if (v < min)
			throw new IllegalArgumentException("Value is less than minimum! " + v + " < " + min);
		if (v > max)
			throw new IllegalArgumentException("Value is greater than maximum! " + v + " > " + max);
		
		return v - min;
	}

	@Override
	public Integer getPropertyValue(int index) {
		return Integer.valueOf(index + min);
	}

	@Override
	public String getName() {
		return propertyName;
	}

	@Override
	public int getNumValues() {
		return max - min + 1;
	}

	public boolean equals(IntBlockProperty other) {
		if (other == this)
			return true;
		
		if (other != null && other.min == min && other.max == max)
			return propertyName.equals(other.propertyName);
		return false;
	}

	@Override
	public boolean equals(IBlockProperty<Integer> other) {
		if (!(other instanceof IntBlockProperty))
			return false;
		
		return equals((IntBlockProperty)other);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof IntBlockProperty))
			return false;
		
		return equals((IntBlockProperty)other);
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public String toString() {
		return "IntBlockProperty[name=\"" + propertyName + "\", min=" + min + ", max=" + max + "]";
	}
}
