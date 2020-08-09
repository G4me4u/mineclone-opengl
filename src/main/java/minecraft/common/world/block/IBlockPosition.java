package minecraft.common.world.block;

import minecraft.common.world.Direction;

public interface IBlockPosition {

	public int getX();
	
	public int getY();
	
	public int getZ();

	default public IBlockPosition getOffset(Direction dir) {
		return getOffset(dir, 1);
	}
	
	default public IBlockPosition getOffset(Direction dir, int amount) {
		return getOffset(dir.getOffsetX() * amount, dir.getOffsetY() * amount, dir.getOffsetZ() * amount);
	}
	
	public IBlockPosition getOffset(int xo, int yo, int zo);
	
	public ImmutableBlockPosition toImmutable();
	
	default public IBlockPosition north() {
		return getOffset(Direction.NORTH);
	}
	
	default public IBlockPosition south() {
		return getOffset(Direction.SOUTH);
	}
	
	default public IBlockPosition west() {
		return getOffset(Direction.WEST);
	}
	
	default public IBlockPosition east() {
		return getOffset(Direction.EAST);
	}
	
	default public IBlockPosition down() {
		return getOffset(Direction.DOWN);
	}
	
	default public IBlockPosition up() {
		return getOffset(Direction.UP);
	}
	
	default public boolean equals(int x, int y, int z) {
		return getX() == x && getY() == y && getZ() == z;
	}

	default public boolean equals(IBlockPosition blockPos) {
		return equals(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
}
