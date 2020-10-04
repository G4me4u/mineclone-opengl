package mineclone.common.world.block.state;

public interface IBlockProperty<T> {

	public String getName();

	public int getValueIndex(T value);

	public T getValue(int index);

	public int getValueCount();
	
}
