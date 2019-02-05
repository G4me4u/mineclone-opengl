package com.g4mesoft.minecraft.world.block;

import com.g4mesoft.minecraft.world.Direction;

public interface IBlockPosition {

	public int getX();
	
	public int getY();
	
	public int getZ();

	public BlockPosition getOffset(Direction face);

	public BlockPosition getOffset(int xo, int yo, int zo);
	
}
