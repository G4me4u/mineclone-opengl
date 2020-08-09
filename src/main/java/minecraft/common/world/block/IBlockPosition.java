package minecraft.common.world.block;

import minecraft.common.world.Direction;

public interface IBlockPosition {

	public int getX();
	
	public int getY();
	
	public int getZ();
	
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
	
	default public IBlockPosition getOffset(Direction dir) {
		return getOffset(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());
	}
	
	default public IBlockPosition getOffset(Direction dir, int amount) {
		return getOffset(dir.getOffsetX() * amount, dir.getOffsetY() * amount, dir.getOffsetZ() * amount);
	}

	public IBlockPosition getOffset(int xo, int yo, int zo);
	
	public static int hashCode(IBlockPosition pos) {
		return 31 * (pos.getX() + 31 * (pos.getY() + 31 * pos.getZ()));
	}
	
	default public boolean equals(int x, int y, int z) {
		return (getX() == x && getY() == y && getZ() == z);
	}

	default public boolean equals(IBlockPosition pos) {
		return equals(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public ImmutableBlockPosition toImmutable();
	
}
