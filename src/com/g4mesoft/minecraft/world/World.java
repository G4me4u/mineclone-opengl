package com.g4mesoft.minecraft.world;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.world.block.Block;
import com.g4mesoft.minecraft.world.block.BlockPosition;
import com.g4mesoft.minecraft.world.block.IBlockPosition;
import com.g4mesoft.minecraft.world.block.state.BlockState;
import com.g4mesoft.minecraft.world.entity.PlayerEntity;
import com.g4mesoft.world.phys.AABB3;

public class World {
	
	private static final int WORLD_HEIGHT = 128;
	
	private final int width;
	private final int depth;
	
	private boolean dirty;
	
	private final BlockState[] blocks;
	
	private BlockRay blockRay;
	private PlayerEntity player;
	
	public World(int width, int depth) {
		this.width = width;
		this.depth = depth;
	
		blocks = new BlockState[width * depth * WORLD_HEIGHT];
		
		BlockPosition pos = new BlockPosition();
		for (pos.x = 0; pos.x < width; pos.x++) {
			for (pos.z = 0; pos.z < depth; pos.z++) {
				for (pos.y = 0; pos.y < WORLD_HEIGHT; pos.y++) {
					setBlock(pos, pos.y > 5 ? Blocks.AIR_BLOCK : Blocks.STONE_BLOCK);
				}
			}
		}
		
		blockRay = new BlockRay(this, 0.01f);
		player = new PlayerEntity(this);
	}
	
	public BlockHitResult castBlockRay(float x, float y, float z, Vec3f dir) {
		return blockRay.castRay(x, y, z, dir);
	}
	
	public BlockState getBlockState(IBlockPosition blockPos) {
		if (!isInBounds(blockPos))
			return Blocks.AIR_BLOCK.getDefaultState();
		return blocks[getBlockIndex(blockPos)];
	}
	
	public void setBlock(IBlockPosition blockPos, Block block) {
		setBlockState(blockPos, block.getDefaultState());
	}

	public void setBlockState(IBlockPosition blockPos, BlockState state) {
		if (isInBounds(blockPos)) {
			int index = getBlockIndex(blockPos);
			
			if (state != blocks[index]) {
				blocks[index] = state;
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
