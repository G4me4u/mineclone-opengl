package minecraft.common.world.block.state;

public class BooleanBlockProperty implements IBlockProperty<Boolean> {

	private final String propertyName;
	
	private final int hash;
	
	public BooleanBlockProperty(String propertyName) {
		this.propertyName = propertyName;
		
		hash = propertyName.hashCode();
	}
	
	@Override
	public int getPropertyValueIndex(Boolean value) {
		return value.booleanValue() ? 1 : 0;
	}

	@Override
	public Boolean getPropertyValue(int index) {
		return index != 0 ? true : false;
	}

	@Override
	public String getName() {
		return propertyName;
	}

	@Override
	public int getNumValues() {
		return 2;
	}

	public boolean equals(BooleanBlockProperty other) {
		if (other == this)
			return true;
		
		return other != null && propertyName.equals(other.propertyName);
	}

	@Override
	public boolean equals(IBlockProperty<Boolean> other) {
		if (!(other instanceof BooleanBlockProperty))
			return false;
		
		return equals((BooleanBlockProperty)other);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof BooleanBlockProperty))
			return false;
		
		return equals((BooleanBlockProperty)other);
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public String toString() {
		return "BooleanBlockProperty[name=\"" + propertyName + "\"]";
	}
}
