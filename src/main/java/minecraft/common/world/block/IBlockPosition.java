package minecraft.common.world.block;

import minecraft.common.world.Direction;

public interface IBlockPosition {

	public int getX();
	
	public int getY();
	
	public int getZ();

	default public IBlockPosition getOffset(Direction dir) {
		return getOffset(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());
	}
	
	public IBlockPosition getOffset(int xo, int yo, int zo);
	
	public ImmutableBlockPosition toImmutable();
	
	default public boolean equals(int x, int y, int z) {
		return getX() == x && getY() == y && getZ() == z;
	}

	default public boolean equals(IBlockPosition blockPos) {
		return equals(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
}
