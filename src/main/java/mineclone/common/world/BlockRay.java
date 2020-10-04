package mineclone.common.world;

import mineclone.common.math.LinMath;
import mineclone.common.math.Mat4;
import mineclone.common.math.Vec3;
import mineclone.common.world.block.MutableBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class BlockRay {

	public static final float DEFAULT_STEP_SIZE = 0.1f;
	public static final float DEFAULT_MAX_DIST = 5.0f;
	
	private final IWorld world;
	
	private final float stepSize;
	private final float maxDist;
	
	public BlockRay(IWorld world) {
		this(world, DEFAULT_STEP_SIZE, DEFAULT_MAX_DIST);
	}

	public BlockRay(IWorld world, float stepSize) {
		this(world, stepSize, DEFAULT_MAX_DIST);
	}

	public BlockRay(IWorld world, float stepSize, float maxDist) {
		if (stepSize < LinMath.EPSILON)
			throw new IllegalArgumentException("Step-size must be positive and larger than " + LinMath.EPSILON);

		this.world = world;
		
		this.stepSize = stepSize;
		this.maxDist = maxDist;
	}
	
	public BlockHitResult castRay(Vec3 pos, Vec3 dir) {
		return castRay(pos.x, pos.y, pos.z, dir);
	}

	public BlockHitResult castRay(Mat4 viewMatrix) {
		Vec3 forward = new Vec3(viewMatrix.m20, viewMatrix.m21, viewMatrix.m22);
		return castRay(viewMatrix.m30, viewMatrix.m31, viewMatrix.m32, forward);
	}

	public BlockHitResult castRay(float x, float y, float z, Vec3 dir) {
		dir = new Vec3(dir).normalize().mul(stepSize);
	
		MutableBlockPosition pos = new MutableBlockPosition();
		
		boolean first = false;
		
		float dist = 0.0f;
		while (dist < maxDist) {
			int xx = (int)x;
			int yy = (int)y;
			int zz = (int)z;
			
			if (first || !pos.equals(xx, yy, zz)) {
				first = false;
				
				pos.x = xx;
				pos.y = yy;
				pos.z = zz;
				
				IBlockState state = world.getBlockState(pos);
				
				if (!state.isAir()) {
					Vec3 hitOffset = new Vec3(x - xx - 0.5f, y - yy - 0.5f, z - zz - 0.5f);
					Direction face = Direction.fromVector(hitOffset);
					
					return new BlockHitResult(pos, x, y, z, face);
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
