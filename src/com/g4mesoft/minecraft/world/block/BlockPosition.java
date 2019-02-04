package com.g4mesoft.minecraft.world.block;

public class BlockPosition {

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
	
	public BlockPosition(BlockPosition blockPos) {
		this.x = blockPos.x;
		this.y = blockPos.y;
		this.z = blockPos.z;
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
	
	public BlockPosition getOffsetPosition(int xo, int yo, int zo) {
		return new BlockPosition(x + xo, y + yo, z + zo);
	}
}
