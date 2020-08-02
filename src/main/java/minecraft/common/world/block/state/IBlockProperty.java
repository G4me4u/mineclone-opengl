package minecraft.common.world.block.state;

public interface IBlockProperty<T> {

	public int getPropertyValueIndex(T value);

	public T getPropertyValue(int index);

	public String getName();

	public int getNumValues();
	
	public boolean equals(IBlockProperty<T> other);
	
}
