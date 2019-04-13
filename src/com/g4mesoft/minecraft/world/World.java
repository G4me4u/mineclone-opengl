package com.g4mesoft.minecraft.world;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.MinecraftApp;
import com.g4mesoft.minecraft.world.block.Block;
import com.g4mesoft.minecraft.world.block.BlockPosition;
import com.g4mesoft.minecraft.world.block.IBlockPosition;
import com.g4mesoft.minecraft.world.block.state.BlockState;
import com.g4mesoft.minecraft.world.entity.PlayerEntity;
import com.g4mesoft.world.phys.AABB3;

public class World {
	
	public static final int WORLD_HEIGHT = WorldChunk.CHUNK_SIZE * 1;
	
	public static final int CHUNKS_X = 2;
	public static final int CHUNKS_Z = 2;
	
	private final MinecraftApp app;
	
	private final WorldChunk[] chunks;
	
	private BlockRay blockRay;
	private PlayerEntity player;
	
	public World(MinecraftApp app) {
		this.app = app;

		chunks = new WorldChunk[CHUNKS_X * CHUNKS_Z];
		
		int i = 0;
		for (int cz = 0; cz < CHUNKS_Z; cz++) {
			for (int cx = 0; cx < CHUNKS_X; cx++) {
				chunks[i++] = new WorldChunk(cx, cz);
			}
		}
		
		blockRay = new BlockRay(this, 0.01f);
		player = new PlayerEntity(this);
	}
	
	public BlockHitResult castBlockRay(float x, float y, float z, Vec3f dir) {
		return blockRay.castRay(x, y, z, dir);
	}
	
	public WorldChunk getChunk(IBlockPosition blockPos) {
		if (blockPos.getY() < 0 || blockPos.getY() >= WORLD_HEIGHT)
			return null;
		
		int chunkX = blockPos.getX() / WorldChunk.CHUNK_SIZE;
		int chunkZ = blockPos.getZ() / WorldChunk.CHUNK_SIZE;
		
		return getChunk(chunkX, chunkZ);
	}

	public WorldChunk getChunk(int chunkX, int chunkZ) {
		if (chunkX < 0 || chunkX >= CHUNKS_X)
			return null;
		if (chunkZ < 0 || chunkZ >= CHUNKS_Z)
			return null;
		
		return chunks[chunkX + chunkZ * CHUNKS_X];
	}
	
	public BlockState getBlockState(IBlockPosition blockPos) {
		WorldChunk chunk = getChunk(blockPos);
		if (chunk == null)
			return Blocks.AIR_BLOCK.getDefaultState();
		return chunk.getBlockState(blockPos);
	}
	
	public void setBlock(IBlockPosition blockPos, Block block) {
		setBlockState(blockPos, block.getDefaultState());
	}

	public void setBlockState(IBlockPosition blockPos, BlockState state) {
		WorldChunk chunk = getChunk(blockPos);
		if (chunk != null) {
			if (chunk.setBlockState(blockPos, state))
				markDirty(blockPos);
		}
	}
	
	private void markDirty(IBlockPosition blockPos) {
		app.getWorldRenderer().markDirty(blockPos);
	}
	
	public boolean isLoadedBlock(IBlockPosition blockPos) {
		return getChunk(blockPos) != null;
	}

	public boolean isLoadedBlock(int chunkX, int chunkZ) {
		return getChunk(chunkX, chunkZ) != null;
	}
	
	public void update() {
		player.update();
	}

	public int getHeight() {
		return WORLD_HEIGHT;
	}
	
	public PlayerEntity getPlayer() {
		return player;
	}

	public List<AABB3> getBlockHitboxes(AABB3 hitbox) {
		List<AABB3> hitboxes = new ArrayList<AABB3>();
		
		BlockPosition pos = new BlockPosition();
		
		int x1 = (int)hitbox.x1;
		int y1 = (int)hitbox.y1;
		int z1 = (int)hitbox.z1;
		
		for (pos.x = (int)hitbox.x0; pos.x <= x1; pos.x++) {
			for (pos.y = (int)hitbox.y0; pos.y <= y1; pos.y++) {
				for (pos.z = (int)hitbox.z0; pos.z <= z1; pos.z++) {
					BlockState state = getBlockState(pos);
					state.getBlock().getEntityHitboxes(this, pos, state, hitboxes);
				}
			}
		}
		
		return hitboxes;
	}
}
