package com.g4mesoft.minecraft.world;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.world.block.BlockPosition;
import com.g4mesoft.minecraft.world.block.IBlockPosition;
import com.g4mesoft.minecraft.world.entity.PlayerEntity;
import com.g4mesoft.world.phys.AABB3;

public class World {
	
	private static final int WORLD_HEIGHT = 128;
	
	public static final int BLOCK_AIR = 0;
	public static final int BLOCK_SOLID = 1;
	
	private final int width;
	private final int depth;
	
	private boolean dirty;
	
	private final int[] blocks;
	
	private BlockRay blockRay;
	private PlayerEntity player;
	
	public World(int width, int depth) {
		this.width = width;
		this.depth = depth;
	
		blocks = new int[width * depth * WORLD_HEIGHT];
		
		BlockPosition pos = new BlockPosition();
		for (pos.z = 0; pos.z < depth; pos.z++) {
			for (pos.x = 0; pos.x < width; pos.x++) {
				for (pos.y = 0; pos.y < WORLD_HEIGHT; pos.y++) {
					setBlock(pos, pos.y > 5 ? BLOCK_AIR : BLOCK_SOLID);
				}
			}
		}
		
		blockRay = new BlockRay(this);
		player = new PlayerEntity(this);
	}
	
	public BlockHitResult castBlockRay(float x, float y, float z, Vec3f dir) {
		return blockRay.castRay(x, y, z, dir);
	}
	
	public int getBlock(IBlockPosition blockPos) {
		if (!isInBounds(blockPos))
			return BLOCK_AIR;
		return blocks[getBlockIndex(blockPos)];
	}
	
	public void setBlock(IBlockPosition blockPos, int block) {
		if (isInBounds(blockPos)) {
			int index = getBlockIndex(blockPos);
			
			if (block != blocks[index]) {
				blocks[index] = block;
				markDirty(blockPos);
			}
		}
	}
	
	private void markDirty(IBlockPosition blockPos) {
		dirty = true;
	}
	
	private boolean isInBounds(IBlockPosition blockPos) {
		if (blockPos.getX() < 0 || blockPos.getX() >= width)
			return false;
		if (blockPos.getY() < 0 || blockPos.getY() >= WORLD_HEIGHT)
			return false;
		if (blockPos.getZ() < 0 || blockPos.getZ() >= depth)
			return false;
		
		return true;
	}
	
	private int getBlockIndex(IBlockPosition blockPos) {
		return blockPos.getX() + width * (blockPos.getY() + blockPos.getZ() * WORLD_HEIGHT);
	}
	
	public void update() {
		player.update();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return WORLD_HEIGHT;
	}

	public int getDepth() {
		return depth;
	}
	
	public boolean isDirty() {
		return dirty;
	}

	public void noLongerDirty() {
		dirty = false;
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
		
		for (pos.z = (int)hitbox.z0; pos.z <= z1; pos.z++) {
			for (pos.y = (int)hitbox.y0; pos.y <= y1; pos.y++) {
				for (pos.x = (int)hitbox.x0; pos.x <= x1; pos.x++) {
					if (getBlock(pos) != BLOCK_AIR) {
						float x = pos.x;
						float y = pos.y;
						float z = pos.z;
						hitboxes.add(new AABB3(x, y, z, x + 1.0f, y + 1.0f, z + 1.0f));
					}
				}
			}
		}
		
		return hitboxes;
	}
}
