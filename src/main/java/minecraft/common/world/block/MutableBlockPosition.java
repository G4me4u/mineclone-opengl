package minecraft.common.world.block;

public class MutableBlockPosition implements IBlockPosition {

	public int x;
	public int y;
	public int z;

	public MutableBlockPosition() {
		this(0, 0, 0);
	}
	
	public MutableBlockPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public MutableBlockPosition(IBlockPosition blockPos) {
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
	public MutableBlockPosition getOffset(int xo, int yo, int zo) {
		return new MutableBlockPosition(x + xo, y + yo, z + zo);
	}

	@Override
	public ImmutableBlockPosition toImmutable() {
		return new ImmutableBlockPosition(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IBlockPosition)
			return equals((IBlockPosition)obj);
		return false;
	}
}
