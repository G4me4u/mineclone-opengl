package com.g4mesoft.minecraft.world.block;

import com.g4mesoft.math.Vec3i;
import com.g4mesoft.minecraft.world.Direction;

public interface IBlockPosition {

	public int getX();
	
	public int getY();
	
	public int getZ();

	default public IBlockPosition getOffset(Direction dir) {
		Vec3i normal = dir.getNormal();
		return getOffset(normal.x, normal.y, normal.z);
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
