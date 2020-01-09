package com.g4mesoft.minecraft.world.entity;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.input.mouse.MouseInputListener;
import com.g4mesoft.math.Mat3f;
import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.controller.PlayerCamera;
import com.g4mesoft.minecraft.controller.PlayerController;
import com.g4mesoft.minecraft.controller.PlayerHotbar;
import com.g4mesoft.minecraft.world.BlockHitResult;
import com.g4mesoft.minecraft.world.Blocks;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.WorldChunk;
import com.g4mesoft.minecraft.world.block.Block;
import com.g4mesoft.minecraft.world.block.IBlockPosition;
import com.g4mesoft.minecraft.world.block.state.BlockState;
import com.g4mesoft.world.phys.AABB3;

public class PlayerEntity extends Entity {

	private static final int INTERACT_INTERVAL = 4;
	
	private final PlayerCamera camera;
	private final PlayerController controller;
	private final PlayerHotbar hotbar;
	
	public float yaw;
	public float pitch;
	
	public float prevYaw;
	public float prevPitch;
	
	private int interactTimer;
	
	public PlayerEntity(World world) {
		super(world);
		
		camera = new PlayerCamera(0.1f);
		controller = new PlayerController();
		hotbar = new PlayerHotbar();
		
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
		
		hotbar.update();
		
		boolean remove = MouseInputListener.MOUSE_LEFT.isActive();
		boolean place = MouseInputListener.MOUSE_RIGHT.isActive();
		if (remove || place) {
			interactTimer--;

			if (interactTimer <= 0) {
				Mat3f rot = new Mat3f().rotateX(-yaw).rotateY(-pitch);
				Vec3f dir = new Vec3f(-rot.m02, -rot.m12, -rot.m22);
				BlockHitResult hitResult = world.castBlockRay(eyeX, eyeY, eyeZ, dir);
				
				if (hitResult != null) {
					boolean success = false;
					
					if (remove) {
						world.setBlock(hitResult.blockPos, Blocks.AIR_BLOCK);
						success = true;
					} else {
						IBlockPosition placePos = hitResult.blockPos.getOffset(hitResult.face);
						
						BlockState placeState = hotbar.getHotbarBlock();
						Block placeBlock = placeState.getBlock();
						
						List<AABB3> hitboxes = new ArrayList<AABB3>();
						placeBlock.getEntityHitboxes(world, placePos, placeState, hitboxes);
						
						AABB3 playerHitbox = getHitbox();
						boolean insidePlayer = false;
						for (AABB3 hitbox : hitboxes) {
							if (playerHitbox.collides(hitbox)) {
								insidePlayer = true;
								break;
							}
						}
						
						if (!insidePlayer) {
							world.setBlockState(placePos, placeState);
							success = true;
						}
					}
					
					if (success)
						interactTimer = INTERACT_INTERVAL;
				}
			}
		} else {
			interactTimer = 0;
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
