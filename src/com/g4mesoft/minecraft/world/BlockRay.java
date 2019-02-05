package com.g4mesoft.minecraft.world;

import com.g4mesoft.math.MathUtils;
import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.world.block.BlockPosition;

public class BlockRay {

	public static final float DEFAULT_STEP_SIZE = 0.1f;
	public static final float DEFAULT_MAX_DIST = 5.0f;
	
	private final World world;
	
	private final float stepSize;
	private final float maxDist;
	
	public BlockRay(World world) {
		this(world, DEFAULT_STEP_SIZE, DEFAULT_MAX_DIST);
	}

	public BlockRay(World world, float stepSize, float maxDist) {
		if (stepSize < MathUtils.EPSILON)
			throw new IllegalArgumentException("Step-size must be positive and larger than " + MathUtils.EPSILON);

		this.world = world;
		
		this.stepSize = stepSize;
		this.maxDist = maxDist;
	}
	
	public BlockHitResult castRay(Vec3f pos, Vec3f dir) {
		return castRay(pos.x, pos.y, pos.z, dir);
	}

	public BlockHitResult castRay(float x, float y, float z, Vec3f dir) {
		dir = new Vec3f(dir).normalize().mul(stepSize);
	
		BlockPosition blockPos = new BlockPosition();
		
		boolean first = false;
		
		float dist = 0.0f;
		while (dist < maxDist) {
			int xx = (int)x;
			int yy = (int)y;
			int zz = (int)z;
			
			if (first || !blockPos.equals(xx, yy, zz)) {
				first = false;
				
				blockPos.x = xx;
				blockPos.y = yy;
				blockPos.z = zz;
				
				int block = world.getBlock(blockPos);
				if (block != World.BLOCK_AIR) {
					Vec3f hitOffset = new Vec3f(x - xx - 0.5f, y - yy - 0.5f, z - zz - 0.5f);
					Direction face = Direction.fromVector(hitOffset);
					return new BlockHitResult(blockPos, x, y, z, face);
				}
			}
			
			x += dir.x;
			y += dir.y;
			z += dir.z;
			
			dist += stepSize;
		}
		
		return null;
	}
}
