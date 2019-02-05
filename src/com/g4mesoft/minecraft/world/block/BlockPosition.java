package com.g4mesoft.minecraft.world.block;

import com.g4mesoft.math.Vec3i;
import com.g4mesoft.minecraft.world.Direction;

public class BlockPosition implements IBlockPosition {

	public int x;
	public int y;
	public int z;

	public BlockPosition() {
		this(0, 0, 0);
	}
	
	public BlockPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BlockPosition(IBlockPosition blockPos) {
		this.x = blockPos.getX();
		this.y = blockPos.getY();
		this.z = blockPos.getZ();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}

	public BlockPosition getOffset(Direction dir) {
		Vec3i normal = dir.getNormal();
		return getOffset(normal.x, normal.y, normal.z);
	}
	
	public BlockPosition getOffset(int xo, int yo, int zo) {
		return new BlockPosition(x + xo, y + yo, z + zo);
	}

	public boolean equals(int x, int y, int z) {
		return this.x == x && this.y == y && this.z == z;
	}
}
