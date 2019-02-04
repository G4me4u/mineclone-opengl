package com.g4mesoft.minecraft.world.entity;

import java.util.List;

import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.world.phys.AABB3;

public abstract class Entity {

	private final World world;
	private final AABB3 hitbox;
	
	public float x;
	public float y;
	public float z;

	public float px;
	public float py;
	public float pz;
	
	protected float vx;
	protected float vy;
	protected float vz;
	
	public Entity(World world) {
		this.world = world;
	
		hitbox = createHitbox();
		hitbox.moveCenterToOrigin();
	}

	protected AABB3 createHitbox() {
		return new AABB3(0.0f, 0.0f, 0.0f, 0.6f, 1.8f, 0.6f);
	}
	
	public AABB3 getHitbox() {
		return hitbox;
	}

	public void update() {
		this.px = x;
		this.py = y;
		this.pz = z;
		
		float friction = 0.5f;
		vx *= friction;
		vy *= friction;
		vz *= friction;

		finishMovement(vx, vy, vz);
	}
	
	private void finishMovement(float dx, float dy, float dz) {
		float oldDy = dy;
		
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

		this.x = hitbox.getCenterX();
		this.y = hitbox.y0 + 1.6f;
		this.z = hitbox.getCenterZ();
		
		boolean onGround = dy > oldDy;
		if (onGround)
			vy = 0.0f;
	}
}
