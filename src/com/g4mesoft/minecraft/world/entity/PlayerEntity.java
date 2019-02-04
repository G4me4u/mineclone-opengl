package com.g4mesoft.minecraft.world.entity;

import com.g4mesoft.minecraft.controller.PlayerCamera;
import com.g4mesoft.minecraft.controller.PlayerController;
import com.g4mesoft.minecraft.world.World;

public class PlayerEntity extends Entity {

	private final PlayerCamera camera;
	private final PlayerController controller;
	
	public float yaw;
	public float pitch;
	
	public float prevYaw;
	public float prevPitch;
	
	public PlayerEntity(World world) {
		super(world);
		
		camera = new PlayerCamera(0.1f);
		controller = new PlayerController();
	}
	
	@Override
	public void update() {
		this.prevYaw = yaw;
		this.prevPitch = pitch;
		
		camera.update();
		if (camera.hasRotationChanged()) {
			yaw = camera.yaw;
			pitch = camera.pitch;
		}
		
		controller.update(pitch);
		if (controller.hasMoved()) {
			vx += controller.getMoveX();
			vy += controller.getMoveY();
			vz += controller.getMoveZ();
		}

		super.update();
	}
}
