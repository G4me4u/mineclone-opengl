package mineclone.common.world.block;

import mineclone.common.world.Direction;

public interface IBlockPosition {

	public int getX();
	
	public int getY();
	
	public int getZ();
	
	default public IBlockPosition north() {
		return offset(Direction.NORTH);
	}
	
	default public IBlockPosition south() {
		return offset(Direction.SOUTH);
	}
	
	default public IBlockPosition west() {
		return offset(Direction.WEST);
	}
	
	default public IBlockPosition east() {
		return offset(Direction.EAST);
	}
	
	default public IBlockPosition down() {
		return offset(Direction.DOWN);
	}
	
	default public IBlockPosition up() {
		return offset(Direction.UP);
	}
	
	default public IBlockPosition offset(Direction dir) {
		return offset(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());
	}
	
	default public IBlockPosition offset(Direction dir, int amount) {
		return offset(dir.getOffsetX() * amount, dir.getOffsetY() * amount, dir.getOffsetZ() * amount);
	}

	public IBlockPosition offset(int xo, int yo, int zo);
	
	public static int hashCode(IBlockPosition pos) {
		int hash = 0;
		hash = 31 * (hash + pos.getZ());
		hash = 31 * (hash + pos.getY());
		hash = 31 * (hash + pos.getX());
		return hash;
	}
	
	default public boolean equals(int x, int y, int z) {
		return (getX() == x && getY() == y && getZ() == z);
	}

	default public boolean equals(IBlockPosition pos) {
		return equals(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public ImmutableBlockPosition toImmutable();
	
}
