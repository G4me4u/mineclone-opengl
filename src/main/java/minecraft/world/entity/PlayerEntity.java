package minecraft.world.entity;

import java.util.ArrayList;
import java.util.List;

import minecraft.controller.PlayerController;
import minecraft.controller.PlayerHotbar;
import minecraft.input.Mouse;
import minecraft.math.Mat3;
import minecraft.math.Vec3;
import minecraft.world.BlockHitResult;
import minecraft.world.Blocks;
import minecraft.world.EntityHitbox;
import minecraft.world.World;
import minecraft.world.WorldChunk;
import minecraft.world.block.Block;
import minecraft.world.block.IBlockPosition;
import minecraft.world.block.state.BlockState;

public class PlayerEntity extends Entity {

	private static final int INTERACT_INTERVAL = 4;
	
	private final PlayerController controller;

	private final PlayerHotbar hotbar;
	
	public float yaw;
	public float pitch;

	private int interactTimer;
	
	public PlayerEntity(World world, PlayerController controller) {
		super(world);
		
		this.controller = controller;
		
		controller.setPlayer(this);
		
		hotbar = new PlayerHotbar();
		
		float x = World.CHUNKS_X * WorldChunk.CHUNK_SIZE / 2;
		float y = World.WORLD_HEIGHT;
		float z = World.CHUNKS_Z * WorldChunk.CHUNK_SIZE / 2;
		moveHitboxTo(x, y, z);
	}
	
	@Override
	public void update() {
		hotbar.update();
		
		boolean remove = Mouse.isHeld(Mouse.BUTTON_LEFT);
		boolean place = Mouse.isHeld(Mouse.BUTTON_RIGHT);
		
		if (remove || place) {
			interactTimer--;

			if (interactTimer <= 0) {
				float yawRad   = (float)Math.toRadians(yaw);
				float pitchRad = (float)Math.toRadians(pitch);
				
				Mat3 rot = new Mat3().rotateX(-yawRad).rotateY(-pitchRad);
				Vec3 dir = new Vec3(-rot.m02, -rot.m12, -rot.m22);
				BlockHitResult hitResult = world.castBlockRay(eyeX, eyeY, eyeZ, dir);
				
				if (hitResult != null) {
					boolean success = false;
					
					if (remove) {
						world.setBlock(hitResult.blockPos, Blocks.AIR_BLOCK);
						success = true;
					} else {
						IBlockPosition placePos = hitResult.blockPos.getOffset(hitResult.face);
						BlockState placeState = hotbar.getHotbarBlock();
						
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
		
		controller.update();
		
		super.update();
	}
	
	private boolean isBlockInsidePlayer(BlockState state, IBlockPosition blockPos) {
		Block block = state.getBlock();

		List<EntityHitbox> hitboxes = new ArrayList<EntityHitbox>();
		block.getEntityHitboxes(world, blockPos, state, hitboxes);
		
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
	
	public void setRotation(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}
}
