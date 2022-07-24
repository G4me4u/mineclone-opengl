package mineclone.common.world.entity;

import java.util.ArrayList;
import java.util.List;

import mineclone.common.math.LinMath;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.IWorldChunk;

public class PlayerEntity extends Entity {

	protected float yaw;
	protected float pitch;
	
	public PlayerEntity(IWorld world) {
		super(world);

		float x = IWorld.CHUNKS_X * IWorldChunk.CHUNK_SIZE / 2;
		float y = IWorld.CHUNKS_Y * IWorldChunk.CHUNK_SIZE;
		float z = IWorld.CHUNKS_Z * IWorldChunk.CHUNK_SIZE / 2;
		moveHitboxTo(x, y, z);
	}
	
	protected boolean isBlockInsidePlayer(IBlockState state, IBlockPosition pos) {
		IBlock block = state.getBlock();

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
