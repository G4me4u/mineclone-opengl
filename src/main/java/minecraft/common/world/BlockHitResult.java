package minecraft.common.world;

import minecraft.common.world.block.IBlockPosition;

public class BlockHitResult {

	public final IBlockPosition pos;
	
	public final float x;
	public final float y;
	public final float z;
	
	public final Direction face;
	
	public BlockHitResult(IBlockPosition pos, float x, float y, float z, Direction face) {
		this.pos = pos;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.face = face;
	}
}
