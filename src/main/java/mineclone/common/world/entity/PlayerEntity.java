package mineclone.common.world.entity;

import java.util.ArrayList;
import java.util.List;

import mineclone.client.controller.PlayerController;
import mineclone.client.controller.PlayerHotbar;
import mineclone.client.input.Keyboard;
import mineclone.client.input.Mouse;
import mineclone.common.math.LinMath;
import mineclone.common.math.Mat3;
import mineclone.common.math.Vec3;
import mineclone.common.world.BlockHitResult;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.Block;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.IWorldChunk;

public class PlayerEntity extends Entity {

	private static final int INTERACT_INTERVAL = 4;
	
	private final PlayerController controller;

	private final PlayerHotbar hotbar;
	
	protected float yaw;
	protected float pitch;

	private int interactTimer;
	
	public PlayerEntity(IWorld world, PlayerController controller) {
		super(world);
		
		this.controller = controller;
		
		hotbar = new PlayerHotbar();

		float x = IWorld.CHUNKS_X * IWorldChunk.CHUNK_SIZE / 2;
		float y = IWorld.CHUNKS_Y * IWorldChunk.CHUNK_SIZE;
		float z = IWorld.CHUNKS_Z * IWorldChunk.CHUNK_SIZE / 2;
		moveHitboxTo(x, y, z);

		controller.setPlayer(this);
	}
	
	@Override
	public void update() {
		hotbar.update();
		
		boolean remove = Mouse.isHeld(Mouse.BUTTON_LEFT);
		boolean place = Mouse.isHeld(Mouse.BUTTON_RIGHT);
		
		if (remove || place) {
			interactTimer--;

			if (interactTimer <= 0) {
				Mat3 rot = new Mat3().rotateX(-yaw).rotateY(-pitch);
				Vec3 dir = new Vec3(-rot.m02, -rot.m12, -rot.m22);
				BlockHitResult hitResult = world.castBlockRay(eyeX, eyeY, eyeZ, dir);
				
				if (hitResult != null) {
					boolean success = false;
					
					if (remove) {
						world.setBlock(hitResult.pos, Blocks.AIR_BLOCK);
						success = true;
					} else {
						IBlockPosition placePos = hitResult.pos.offset(hitResult.face);
						IBlockState placeState = hotbar.getHotbarBlock().getPlacementState(world, placePos);
						
						if (!isBlockInsidePlayer(placeState, placePos)) {
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
		
		if (Keyboard.isHeld(Keyboard.KEY_P)) {
			Mat3 rot = new Mat3().rotateX(-yaw).rotateY(-pitch);
			Vec3 dir = new Vec3(-rot.m02, -rot.m12, -rot.m22);
			BlockHitResult hitResult = world.castBlockRay(eyeX, eyeY, eyeZ, dir);

			if (hitResult != null) {
				System.out.println(world.getBlockState(hitResult.pos));
			} else {
				System.out.println("NONE");
			}
		}
		
		controller.update();
		
		super.update();
	}
	
	private boolean isBlockInsidePlayer(IBlockState state, IBlockPosition pos) {
		Block block = state.getBlock();

		List<EntityHitbox> hitboxes = new ArrayList<>();
		block.getEntityHitboxes(world, pos, state, hitboxes);
		
		EntityHitbox playerHitbox = getHitbox();

		for (EntityHitbox hitbox : hitboxes) {
			if (playerHitbox.collides(hitbox))
				return true;
		}

		return false;
	}

	public void addVelocity(float vx, float vy, float vz) {
		this.vx += vx;
		this.vy += vy;
		this.vz += vz;
	}
	
	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = LinMath.clamp(yaw, -LinMath.HALF_PI, LinMath.HALF_PI);
	}

	public float getPitch() {
		return pitch;
	}
	
	public void setPitch(float pitch) {
		this.pitch = LinMath.normalizeRadians(pitch);
	}
}
