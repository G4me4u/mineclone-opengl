package com.g4mesoft.minecraft.world.entity;

import java.util.List;

import com.g4mesoft.math.MathUtils;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.world.phys.AABB3;

public abstract class Entity {

	protected final World world;
	private final AABB3 hitbox;
	
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
	
	public Entity(World world) {
		this.world = world;
	
		hitbox = createHitbox();
		hitbox.move(-hitbox.getCenterX(), 0.0f, -hitbox.getCenterZ());
	}

	protected AABB3 createHitbox() {
		return new AABB3(0.0f, 0.0f, 0.0f, 0.6f, 1.8f, 0.6f);
	}
	
	public AABB3 getHitbox() {
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
		
		List<AABB3> blocks = world.getBlockHitboxes(hitbox.expand(dx, dy, dz));
		
		for (AABB3 block : blocks)
			dy = block.clipY(hitbox, dy);
		hitbox.move(0.0f, dy, 0.0f);
		
		for (AABB3 block : blocks)
			dx = block.clipX(hitbox, dx);
		hitbox.move(dx, 0.0f, 0.0f);
		for (AABB3 block : blocks)
			dz = block.clipZ(hitbox, dz);
		hitbox.move(0.0f, 0.0f, dz);

		updateEyePos();
		
		if (!MathUtils.nearZero(oldDx - dx))
			vx = 0.0f;
		if (!MathUtils.nearZero(oldDz - dz))
			vz = 0.0f;

		if (!MathUtils.nearZero(oldDy - dy)) {
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
		eyeY = hitbox.y0 + 1.6f;
		eyeZ = hitbox.getCenterZ();
	}
}
