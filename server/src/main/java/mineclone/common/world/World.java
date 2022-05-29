package mineclone.common.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mineclone.common.math.Vec3;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.MutableBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunkManager;

public abstract class World implements IWorld {
	
	protected final IWorldChunkManager chunkManager;

	protected final Random random;
	protected BlockRay blockRay;
	
	public World(IWorldChunkManager chunkManager) {
		this.chunkManager = chunkManager;
	
		random = new Random();
		blockRay = new BlockRay(this, 0.01f);
	}
	
	@Override
	public BlockHitResult castBlockRay(float x, float y, float z, Vec3 dir) {
		return blockRay.castRay(x, y, z, dir);
	}
	
	@Override
	public IBlockState getBlockState(IBlockPosition pos) {
		return chunkManager.getBlockState(pos);
	}
	
	@Override
	public int getHighestPoint(IBlockPosition pos) {
		return chunkManager.getHighestPoint(pos.getX(), pos.getZ());
	}
	
	@Override
	public boolean isLoadedBlock(IBlockPosition pos) {
		return chunkManager.containsState(pos);
	}
	
	@Override
	public boolean isLoadedChunk(IChunkPosition chunkPos) {
		return chunkManager.containsChunk(chunkPos);
	}
	
	@Override
	public void update() {
	}
	
	@Override
	public List<EntityHitbox> getBlockHitboxes(EntityHitbox hitbox) {
		List<EntityHitbox> hitboxes = new ArrayList<>();
		
		MutableBlockPosition pos = new MutableBlockPosition();
		
		int x1 = (int)hitbox.x1;
		int y1 = (int)hitbox.y1;
		int z1 = (int)hitbox.z1;
		
		for (pos.x = (int)hitbox.x0; pos.x <= x1; pos.x++) {
			for (pos.y = (int)hitbox.y0; pos.y <= y1; pos.y++) {
				for (pos.z = (int)hitbox.z0; pos.z <= z1; pos.z++)
					getBlockState(pos).getEntityHitboxes(this, pos, hitboxes);
			}
		}
		
		return hitboxes;
	}
	
	@Override
	public IWorldChunkManager getChunkManager() {
		return chunkManager;
	}
}
