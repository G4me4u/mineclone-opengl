package minecraft.world.block;

public class ImmutableBlockPosition implements IBlockPosition {

	public final int x;
	public final int y;
	public final int z;

	public ImmutableBlockPosition() {
		this(0, 0, 0);
	}
	
	public ImmutableBlockPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public ImmutableBlockPosition(IBlockPosition blockPos) {
		this.x = blockPos.getX();
		this.y = blockPos.getY();
		this.z = blockPos.getZ();
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public int getZ() {
		return z;
	}

	@Override
	public ImmutableBlockPosition getOffset(int xo, int yo, int zo) {
		return new ImmutableBlockPosition(x + xo, y + yo, z + zo);
	}
	
	@Override
	public ImmutableBlockPosition toImmutable() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IBlockPosition)
			return equals((IBlockPosition)obj);
		return false;
	}
}
