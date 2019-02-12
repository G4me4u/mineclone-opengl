package com.g4mesoft.minecraft.renderer;

import java.util.ArrayList;
import java.util.List;

import com.g4mesoft.graphics3d.AbstractPixelRenderer3D;
import com.g4mesoft.graphics3d.Vertex3D;
import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.BlockPosition;
import com.g4mesoft.minecraft.world.entity.PlayerEntity;

public class WorldRenderer {

	private static final float FOV = 70.0f;
	private static final float NEAR = 0.1f;
	private static final float FAR = 1000.0f;
	
	private final World world;
	
	private final List<Vertex3D> tmpVertices;
	private Vertex3D[] vertices;
	
	private final WorldCamera camera;
	private final WorldShader3D worldShader;
	
	public WorldRenderer(World world) {
		this.world = world;
		
		tmpVertices = new ArrayList<Vertex3D>();
		vertices = null;
	
		camera = new WorldCamera();
		worldShader = new WorldShader3D();
	}
	
	private void tesselateWorld() {
		int width = world.getWidth();
		int height = world.getHeight();
		int depth = world.getDepth();
		
		for (int z = 0; z < depth; z++) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int block = world.getBlock(new BlockPosition(x, y, z));
					if (block != World.BLOCK_AIR) {
						Vertex3D v000 = new Vertex3D(3);
						v000.pos.set(x + 0, y + 0, z + 0, 1.0f);
						v000.storeVec3f(0, new Vec3f(0, 0, 0));
						
						Vertex3D v100 = new Vertex3D(3);
						v100.pos.set(x + 1, y + 0, z + 0, 1.0f);
						v100.storeVec3f(0, new Vec3f(1, 0, 0));

						Vertex3D v010 = new Vertex3D(3);
						v010.pos.set(x + 0, y + 1, z + 0, 1.0f);
						v010.storeVec3f(0, new Vec3f(0, 1, 0));

						Vertex3D v110 = new Vertex3D(3);
						v110.pos.set(x + 1, y + 1, z + 0, 1.0f);
						v110.storeVec3f(0, new Vec3f(1, 1, 0));
	
						Vertex3D v001 = new Vertex3D(3);
						v001.pos.set(x + 0, y + 0, z + 1, 1.0f);
						v001.storeVec3f(0, new Vec3f(0, 0, 1));
						
						Vertex3D v101 = new Vertex3D(3);
						v101.pos.set(x + 1, y + 0, z + 1, 1.0f);
						v101.storeVec3f(0, new Vec3f(1, 0, 1));

						Vertex3D v011 = new Vertex3D(3);
						v011.pos.set(x + 0, y + 1, z + 1, 1.0f);
						v011.storeVec3f(0, new Vec3f(0, 1, 1));

						Vertex3D v111 = new Vertex3D(3);
						v111.pos.set(x + 1, y + 1, z + 1, 1.0f);
						v111.storeVec3f(0, new Vec3f(1, 1, 1));
	
						// Front
						if (world.getBlock(new BlockPosition(x, y, z + 1)) == World.BLOCK_AIR) {
							addTriangle(v101, v011, v001);
							addTriangle(v101, v111, v011);
						}
	
						// Back
						if (world.getBlock(new BlockPosition(x, y, z - 1)) == World.BLOCK_AIR) {
							addTriangle(v000, v110, v100);
							addTriangle(v000, v010, v110);
						}
							
						// BOTTOM
						if (world.getBlock(new BlockPosition(x, y - 1, z)) == World.BLOCK_AIR) {
							addTriangle(v001, v100, v101);
							addTriangle(v001, v000, v100);
						}
						
						// TOP 
						if (world.getBlock(new BlockPosition(x, y + 1, z)) == World.BLOCK_AIR) {
							addTriangle(v010, v111, v110);
							addTriangle(v010, v011, v111);
						}
						
						// LEFT
						if (world.getBlock(new BlockPosition(x - 1, y, z)) == World.BLOCK_AIR) {
							addTriangle(v001, v010, v000);
							addTriangle(v001, v011, v010);
						}
						
						// RIGHT
						if (world.getBlock(new BlockPosition(x + 1, y, z)) == World.BLOCK_AIR) {
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
	
	private void addTriangle(Vertex3D v0, Vertex3D v1, Vertex3D v2) {
		tmpVertices.add(v0);
		tmpVertices.add(v1);
		tmpVertices.add(v2);
	}

	public void displayResized(int newWidth, int newHeight) {
		float aspect = (float)newWidth / newHeight;
		worldShader.setPerspective(FOV, aspect, NEAR, FAR);
	}
	
	public void render(AbstractPixelRenderer3D renderer3d, float dt) {
		if (world.isDirty()) {
			tesselateWorld();
			world.noLongerDirty();
		}
		
		PlayerEntity player = world.getPlayer();

		float x = player.prevEyeX + (player.eyeX - player.prevEyeX) * dt;
		float y = player.prevEyeY + (player.eyeY - player.prevEyeY) * dt;
		float z = player.prevEyeZ + (player.eyeZ - player.prevEyeZ) * dt;
		camera.setPosition(-x, -y, -z);

		float yaw = player.prevYaw + (player.yaw - player.prevYaw) * dt;
		float pitch = player.prevPitch + (player.pitch - player.prevPitch) * dt;
		camera.setRotation(-yaw, -pitch);
		
		worldShader.viewMat.set(camera.getViewMatrix());
		
		renderer3d.setShader(worldShader);
		renderer3d.drawVertices(vertices);
	}
}
