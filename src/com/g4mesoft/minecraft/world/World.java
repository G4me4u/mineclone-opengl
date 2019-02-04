package com.g4mesoft.minecraft.world;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.minecraft.world.block.BlockPosition;
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
	
	private PlayerEntity player;
	
	public World(int width, int depth) {
		this.width = width;
		this.depth = depth;
	
		blocks = new int[width * depth * WORLD_HEIGHT];
		
		for (int z = 0; z < depth; z++) {
			for (int y = 0; y < WORLD_HEIGHT; y++) {
				for (int x = 0; x < width; x++) {
					setBlock(new BlockPosition(x, y, z), y > 64 ? BLOCK_AIR : BLOCK_SOLID);
				}
			}
		}
		
		player = new PlayerEntity(this);
	}
	
	public int getBlock(BlockPosition blockPos) {
		if (!isInBounds(blockPos))
			return BLOCK_AIR;
		return blocks[getBlockIndex(blockPos)];
	}
	
	public void setBlock(BlockPosition blockPos, int block) {
		if (isInBounds(blockPos)) {
			int index = getBlockIndex(blockPos);
			
			if (block != blocks[index]) {
				blocks[index] = block;
				markDirty(blockPos);
			}
		}
	}
	
	private void markDirty(BlockPosition blockPos) {
		dirty = true;
	}
	
	private boolean isInBounds(BlockPosition blockPos) {
		if (blockPos.getX() < 0 || blockPos.getX() >= width)
			return false;
		if (blockPos.getY() < 0 || blockPos.getY() >= WORLD_HEIGHT)
			return false;
		if (blockPos.getZ() < 0 || blockPos.getZ() >= depth)
			return false;
		
		return true;
	}
	
	private int getBlockIndex(BlockPosition blockPos) {
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
