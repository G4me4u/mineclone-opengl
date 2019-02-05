package com.g4mesoft.minecraft.world;

import com.g4mesoft.minecraft.world.block.IBlockPosition;

public class BlockHitResult {

	public final IBlockPosition blockPos;
	
	public final float x;
	public final float y;
	public final float z;
	
	public final Direction face;
	
	public BlockHitResult(IBlockPosition blockPos, float x, float y, float z, Direction face) {
		this.blockPos = blockPos;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.face = face;
	}
}
