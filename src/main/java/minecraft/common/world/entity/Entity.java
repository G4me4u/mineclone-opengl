package minecraft.common.world.entity;

import java.util.List;

import minecraft.common.math.LinMath;
import minecraft.common.world.EntityHitbox;
import minecraft.common.world.IWorld;

public abstract class Entity {

	private static final float EYE_HEIGHT = 1.6f;
	
	protected final IWorld world;
	private final EntityHitbox hitbox;
	
	public float eyeX;
	public float eyeY;
	public float eyeZ;

	public float prevEyeX;
	public float prevEyeY;
	public float prevEyeZ;
	
	public boolean onGround;

	protected float vx;
	protected float vy;
	protected float vz;
	
	public Entity(IWorld world) {
		this.world = world;
	
		hitbox = createHitbox();
		hitbox.move(-hitbox.getCenterX(), 0.0f, -hitbox.getCenterZ());
	}

	protected EntityHitbox createHitbox() {
		return new EntityHitbox(0.0f, 0.0f, 0.0f, 0.6f, 1.8f, 0.6f);
	}
	
	public EntityHitbox getHitbox() {
		return hitbox;
	}

	public void update() {
		this.prevEyeX = eyeX;
		this.prevEyeY = eyeY;
		this.prevEyeZ = eyeZ;
		
		float friction = 0.75f;
		vx *= friction;
		vz *= friction;

		vy -= 0.1f;

		finishMovement(vx, vy, vz);
	}
	
	private void finishMovement(float dx, float dy, float dz) {
		float oldDx = dx;
		float oldDy = dy;
		float oldDz = dz;
		
		List<EntityHitbox> blocks = world.getBlockHitboxes(hitbox.expand(dx, dy, dz));
		
		for (EntityHitbox blockHitbox : blocks)
			dy = blockHitbox.clipY(hitbox, dy);
		hitbox.move(0.0f, dy, 0.0f);
		
		for (EntityHitbox blockHitbox : blocks)
			dx = blockHitbox.clipX(hitbox, dx);
		hitbox.move(dx, 0.0f, 0.0f);
		for (EntityHitbox blockHitbox : blocks)
			dz = blockHitbox.clipZ(hitbox, dz);
		hitbox.move(0.0f, 0.0f, dz);

		updateEyePos();
		
		if (!LinMath.nearZero(oldDx - dx))
			vx = 0.0f;
		if (!LinMath.nearZero(oldDz - dz))
			vz = 0.0f;

		if (!LinMath.nearZero(oldDy - dy)) {
			onGround = oldDy < 0.0f;
			
			vy = 0.0f;
		} else {
			onGround = false;
		}
	}
	
	public void moveHitboxTo(float x, float y, float z) {
		x -= hitbox.getCenterX();
		z -= hitbox.getCenterZ();
		hitbox.move(x, y, z);
		
		updateEyePos();
	}
	
	private void updateEyePos() {
		eyeX = hitbox.getCenterX();
		eyeY = hitbox.y0 + EYE_HEIGHT;
		eyeZ = hitbox.getCenterZ();
	}
}
