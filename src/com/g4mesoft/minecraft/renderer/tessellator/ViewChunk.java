package com.g4mesoft.minecraft.renderer.tessellator;

import com.g4mesoft.graphics3d.IVertexProvider;
import com.g4mesoft.graphics3d.Shape3D;
import com.g4mesoft.graphics3d.VertexTessellator3D;
import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.renderer.WorldRenderer;
import com.g4mesoft.minecraft.world.Blocks;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.WorldChunk;
import com.g4mesoft.minecraft.world.block.Block;
import com.g4mesoft.minecraft.world.block.BlockPosition;
import com.g4mesoft.minecraft.world.block.state.BlockState;

public class ViewChunk {

	public static final int CHUNK_SIZE = WorldChunk.CHUNK_SIZE;
	
	private final WorldRenderer worldRenderer;
	
	private final int chunkX;
	private final int chunkY;
	private final int chunkZ;
	
	private final Vec3f center;

	private boolean dirty;
	private IVertexProvider vertices;
	
	public ViewChunk(WorldRenderer worldRenderer, int chunkX, int chunkY, int chunkZ) {
		this.worldRenderer = worldRenderer;
		
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;

		float x = chunkX * CHUNK_SIZE;
		float y = chunkY * CHUNK_SIZE;
		float z = chunkZ * CHUNK_SIZE;
		
		center = new Vec3f(x, y, z).add(CHUNK_SIZE * 0.5f);
	
		dirty = true;
		vertices = null;
	}
	
	public void rebuild(VertexTessellator3D tessellator) {
		World world = worldRenderer.getWorld();
		
		int xc = getX();
		int yc = getY();
		int zc = getZ();
		
		BlockPosition pos = new BlockPosition(xc, yc, zc);
		WorldChunk chunk = world.getChunk(pos);
		
		// Release old memory before rebuilding
		// the chunk.
		vertices = null;

		if (chunk != null) {
			for (int zo = 0; zo < CHUNK_SIZE; zo++) {
				for (int yo = 0; yo < CHUNK_SIZE; yo++) {
					for (int xo = 0; xo < CHUNK_SIZE; xo++) {
						pos.x = xc + xo;
						pos.y = yc + yo;
						pos.z = zc + zo;
						
						BlockState blockState = chunk.getBlockState(pos);
						Block block = blockState.getBlock();
						
						if (block != Blocks.AIR_BLOCK) {
							IBlockModel blockModel = block.getModel(world, pos, blockState);
							if (blockModel != null)
								blockModel.tessellateBlock(world, pos, tessellator);
						}
					}
				}
			}
	
			if (tessellator.getNumVertices() > 0) {
				vertices = tessellator.getVertexProvider(Shape3D.QUADS);
				tessellator.clear();
			}
		}
		
		dirty = false;
	}
	
	public int getChunkX() {
		return chunkX;
	}
	
	public int getX() {
		return chunkX * CHUNK_SIZE;
	}

	public int getChunkY() {
		return chunkY;
	}

	public int getY() {
		return chunkY * CHUNK_SIZE;
	}
	
	public int getChunkZ() {
		return chunkZ;
	}

	public int getZ() {
		return chunkZ * CHUNK_SIZE;
	}
	
	public IVertexProvider getVertices() {
		return vertices;
	}

	public boolean isEmpty() {
		return vertices == null;
	}
	
	public void setDirty() {
		dirty = true;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public Vec3f getCenter() {
		return getCenter(new Vec3f());
	}

	public Vec3f getCenter(Vec3f center) {
		return center.set(this.center);
	}
}
