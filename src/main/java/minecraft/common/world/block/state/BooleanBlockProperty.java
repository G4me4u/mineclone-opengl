package minecraft.common.world.block.state;

public final class BooleanBlockProperty implements IBlockProperty<Boolean> {

	private final String name;
	
	public BooleanBlockProperty(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getValueIndex(Boolean value) {
		return value.booleanValue() ? 1 : 0;
	}

	@Override
	public Boolean getValue(int index) {
		return (index != 0);
	}

	@Override
	public int getValueCount() {
		return 2;
	}

	@Override
	public int hashCode() {
		// The hashCode is cached in java.lang.String.
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;

		if (!(other instanceof BooleanBlockProperty))
			return false;
		
		BooleanBlockProperty property = (BooleanBlockProperty)other;
		
		if (!name.equals(property.name))
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "BooleanBlockProperty[name=\"" + name + "\"]";
	}
}
