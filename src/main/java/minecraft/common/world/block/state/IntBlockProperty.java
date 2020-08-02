package minecraft.common.world.block.state;

public class IntBlockProperty implements IBlockProperty<Integer> {

	private final String name;
	
	private final int min;
	private final int max;
	
	private final int hash;

	public IntBlockProperty(String name, int count) {
		this(name, 0, count - 1);
	}
	
	public IntBlockProperty(String name, int min, int max) {
		this.name = name;
		
		this.min = min;
		this.max = max;
	
		hash = 31 * (31 * name.hashCode() + min) + max;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getValueIndex(Integer value) {
		int v = value.intValue();
		if (v < min)
			throw new IllegalArgumentException("Value is less than minimum! " + v + " < " + min);
		if (v > max)
			throw new IllegalArgumentException("Value is greater than maximum! " + v + " > " + max);
		
		return v - min;
	}

	@Override
	public Integer getValue(int index) {
		return Integer.valueOf(index + min);
	}

	@Override
	public int getValueCount() {
		return max - min + 1;
	}

	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		
		if (!(other instanceof IntBlockProperty))
			return false;
		
		IntBlockProperty property = (IntBlockProperty)other;
		
		if (min != property.min)
			return false;
		if (max != property.max)
			return false;
		if (!name.equals(property.name))
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "IntBlockProperty[name=\"" + name + "\", min=" + min + ", max=" + max + "]";
	}
}
