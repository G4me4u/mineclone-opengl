package com.g4mesoft.minecraft.renderer;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.graphics3d.Vertex3D;
import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.world.Blocks;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.WorldChunk;
import com.g4mesoft.minecraft.world.block.BlockPosition;

public class ViewChunk {

	public static final int CHUNK_SIZE = WorldChunk.CHUNK_SIZE;
	
	private final WorldRenderer worldRenderer;
	
	private final int chunkX;
	private final int chunkY;
	private final int chunkZ;
	
	private final Vec3f center;

	private final List<Vertex3D> tmpVertices;

	private boolean dirty;
	private Vertex3D[] vertices;
	
	public ViewChunk(WorldRenderer worldRenderer, int chunkX, int chunkY, int chunkZ) {
		this.worldRenderer = worldRenderer;
		
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;

		float x = chunkX * CHUNK_SIZE;
		float y = chunkY * CHUNK_SIZE;
		float z = chunkZ * CHUNK_SIZE;
		
		center = new Vec3f(x, y, z).add(CHUNK_SIZE * 0.5f);
	
		tmpVertices = new ArrayList<Vertex3D>();
		
		dirty = true;
		vertices = new Vertex3D[0];
	}
	
	public void rebuild() {
		World world = worldRenderer.getWorld();
		
		int xc = getX();
		int yc = getY();
		int zc = getZ();
		
		BlockPosition pos = new BlockPosition(xc, yc, zc);
		WorldChunk chunk = world.getChunk(pos);
		
		if (chunk != null) {
			for (int zo = 0; zo < CHUNK_SIZE; zo++) {
				for (int yo = 0; yo < CHUNK_SIZE; yo++) {
					for (int xo = 0; xo < CHUNK_SIZE; xo++) {
						pos.x = xc + xo;
						pos.y = yc + yo;
						pos.z = zc + zo;
						
						if (chunk.getBlockState(pos).getBlock() != Blocks.AIR_BLOCK) {
							Vertex3D v000 = new Vertex3D(3);
							Vertex3D v100 = new Vertex3D(3);
							Vertex3D v010 = new Vertex3D(3);
							Vertex3D v110 = new Vertex3D(3);
							Vertex3D v001 = new Vertex3D(3);
							Vertex3D v101 = new Vertex3D(3);
							Vertex3D v011 = new Vertex3D(3);
							Vertex3D v111 = new Vertex3D(3);
	
							v000.pos.set(pos.x + 0, pos.y + 0, pos.z + 0, 1.0f);
							v100.pos.set(pos.x + 1, pos.y + 0, pos.z + 0, 1.0f);
							v010.pos.set(pos.x + 0, pos.y + 1, pos.z + 0, 1.0f);
							v110.pos.set(pos.x + 1, pos.y + 1, pos.z + 0, 1.0f);
							v001.pos.set(pos.x + 0, pos.y + 0, pos.z + 1, 1.0f);
							v101.pos.set(pos.x + 1, pos.y + 0, pos.z + 1, 1.0f);
							v011.pos.set(pos.x + 0, pos.y + 1, pos.z + 1, 1.0f);
							v111.pos.set(pos.x + 1, pos.y + 1, pos.z + 1, 1.0f);
	
							v000.storeVec3f(0, new Vec3f(0, 0, 0));
							v100.storeVec3f(0, new Vec3f(1, 0, 0));
							v010.storeVec3f(0, new Vec3f(0, 1, 0));
							v110.storeVec3f(0, new Vec3f(1, 1, 0));
							v001.storeVec3f(0, new Vec3f(0, 0, 1));
							v101.storeVec3f(0, new Vec3f(1, 0, 1));
							v011.storeVec3f(0, new Vec3f(0, 1, 1));
							v111.storeVec3f(0, new Vec3f(1, 1, 1));
		
							// Front
							if (world.getBlockState(new BlockPosition(pos.x, pos.y, pos.z + 1)).getBlock() == Blocks.AIR_BLOCK) {
								addTriangle(v101, v011, v001);
								addTriangle(v101, v111, v011);
							}
		
							// Back
							if (world.getBlockState(new BlockPosition(pos.x, pos.y, pos.z - 1)).getBlock() == Blocks.AIR_BLOCK) {
								addTriangle(v000, v110, v100);
								addTriangle(v000, v010, v110);
							}
								
							// BOTTOM
							if (world.getBlockState(new BlockPosition(pos.x, pos.y - 1, pos.z)).getBlock() == Blocks.AIR_BLOCK) {
								addTriangle(v001, v100, v101);
								addTriangle(v001, v000, v100);
							}
							
							// TOP 
							if (world.getBlockState(new BlockPosition(pos.x, pos.y + 1, pos.z)).getBlock() == Blocks.AIR_BLOCK) {
								addTriangle(v010, v111, v110);
								addTriangle(v010, v011, v111);
							}
							
							// LEFT
							if (world.getBlockState(new BlockPosition(pos.x - 1, pos.y, pos.z)).getBlock() == Blocks.AIR_BLOCK) {
								addTriangle(v001, v010, v000);
								addTriangle(v001, v011, v010);
							}
							
							// RIGHT
							if (world.getBlockState(new BlockPosition(pos.x + 1, pos.y, pos.z)).getBlock() == Blocks.AIR_BLOCK) {
								addTriangle(v100, v111, v101);
								addTriangle(v100, v110, v111);
							}
						}
					}
				}
			}
	
			vertices = tmpVertices.toArray(new Vertex3D[tmpVertices.size()]);
			tmpVertices.clear();
		}
		
		dirty = false;
	}
	
	private void addTriangle(Vertex3D v0, Vertex3D v1, Vertex3D v2) {
		tmpVertices.add(v0);
		tmpVertices.add(v1);
		tmpVertices.add(v2);
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
	
	public Vertex3D[] getVertices() {
		return vertices;
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
