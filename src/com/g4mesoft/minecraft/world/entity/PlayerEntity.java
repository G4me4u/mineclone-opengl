package com.g4mesoft.minecraft.world.entity;

import com.g4mesoft.input.mouse.MouseInputListener;
import com.g4mesoft.math.Mat3f;
import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.controller.PlayerCamera;
import com.g4mesoft.minecraft.controller.PlayerController;
import com.g4mesoft.minecraft.world.BlockHitResult;
import com.g4mesoft.minecraft.world.Blocks;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.WorldChunk;
import com.g4mesoft.minecraft.world.block.BlockPosition;

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
		
		float x = World.CHUNKS_X * WorldChunk.CHUNK_SIZE / 2;
		float y = World.WORLD_HEIGHT;
		float z = World.CHUNKS_Z * WorldChunk.CHUNK_SIZE / 2;
		moveHitboxTo(x, y, z);
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
		
		boolean remove = MouseInputListener.MOUSE_LEFT.isClicked();
		boolean place = MouseInputListener.MOUSE_RIGHT.isClicked();
		if (remove || place) {
			Mat3f rot = new Mat3f().rotateX(-yaw).rotateY(-pitch);
			Vec3f dir = new Vec3f(-rot.m02, -rot.m12, -rot.m22);
			BlockHitResult hitResult = world.castBlockRay(eyeX, eyeY, eyeZ, dir);
			
			if (hitResult != null) {
				if (remove) {
					world.setBlock(hitResult.blockPos, Blocks.AIR_BLOCK);
				} else {
					BlockPosition placePos = hitResult.blockPos.getOffset(hitResult.face);
					world.setBlock(placePos, Blocks.STONE_BLOCK);
				}
			}
		}
		
		controller.update(this);
		if (controller.hasMoved()) {
			vx += controller.getMoveX() * 0.1f;
			vy += controller.getMoveY() * 0.285f;
			vz += controller.getMoveZ() * 0.1f;
		}
		
		super.update();
	}
}
