package com.g4mesoft.minecraft.renderer;

import com.g4mesoft.graphics3d.AbstractPixelRenderer3D;
import com.g4mesoft.graphics3d.Vertex3D;
import com.g4mesoft.math.MathUtils;
import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.WorldChunk;
import com.g4mesoft.minecraft.world.block.IBlockPosition;
import com.g4mesoft.minecraft.world.entity.PlayerEntity;

public class WorldRenderer {

	private static final int CHUNKS_Y = World.WORLD_HEIGHT / WorldChunk.CHUNK_SIZE;
	private static final float CHUNK_SPHERE_RADIUS = MathUtils.sqrt(MathUtils.pow(WorldChunk.CHUNK_SIZE, 3.0f));
	
	private static final float FOV = 70.0f;
	private static final float NEAR = 0.1f;
	private static final float FAR = 1000.0f;
	
	private final World world;
	
	private final WorldCamera camera;
	private final WorldShader3D worldShader;
	
	private final ViewChunk[] chunks;
	
	private final BlockSelectionRenderer selectionRenderer;
	
	public WorldRenderer(World world) {
		this.world = world;
		
		camera = new WorldCamera();
		worldShader = new WorldShader3D(camera);
	
		chunks = new ViewChunk[World.CHUNKS_X * CHUNKS_Y * World.CHUNKS_Z];
		
		int index = 0;
		for (int chunkZ = 0; chunkZ < World.CHUNKS_Z; chunkZ++) {
			for (int chunkY = 0; chunkY < CHUNKS_Y; chunkY++) {
				for (int chunkX = 0; chunkX < World.CHUNKS_X; chunkX++) {
					chunks[index++] = new ViewChunk(this, chunkX, chunkY, chunkZ);
				}
			}
		}
		
		selectionRenderer = new BlockSelectionRenderer(world, camera);
	}
	
	public void displayResized(int newWidth, int newHeight) {
		float aspect = (float)newWidth / newHeight;
		camera.setPerspective(FOV, aspect, NEAR, FAR);
	}

	public void markDirty(IBlockPosition blockPos) {
		int chunkX = blockPos.getX() / ViewChunk.CHUNK_SIZE;
		int chunkY = blockPos.getY() / ViewChunk.CHUNK_SIZE;
		int chunkZ = blockPos.getZ() / ViewChunk.CHUNK_SIZE;

		markChunkDirty(chunkX, chunkY, chunkZ);
	
		int xSub = Math.abs(blockPos.getX() % ViewChunk.CHUNK_SIZE);
		if (xSub == 0) {
			markChunkDirty(chunkX - 1, chunkY, chunkZ);
		} else if (xSub == ViewChunk.CHUNK_SIZE - 1) {
			markChunkDirty(chunkX + 1, chunkY, chunkZ);
		}
		
		int ySub = Math.abs(blockPos.getY() % ViewChunk.CHUNK_SIZE);
		if (ySub == 0) {
			markChunkDirty(chunkX, chunkY - 1, chunkZ);
		} else if (ySub == ViewChunk.CHUNK_SIZE - 1) {
			markChunkDirty(chunkX, chunkY + 1, chunkZ);
		}
		
		int zSub = Math.abs(blockPos.getZ() % ViewChunk.CHUNK_SIZE);
		if (zSub == 0) {
			markChunkDirty(chunkX, chunkY, chunkZ - 1);
		} else if (zSub == ViewChunk.CHUNK_SIZE - 1) {
			markChunkDirty(chunkX, chunkY, chunkZ + 1);
		}
	}
	
	public void markChunkDirty(int chunkX, int chunkY, int chunkZ) {
		if (chunkX < 0 || chunkX >= World.CHUNKS_X)
			return;
		if (chunkY < 0 || chunkY >= CHUNKS_Y)
			return;
		if (chunkZ < 0 || chunkZ >= World.CHUNKS_Z)
			return;
		
		chunks[chunkX + (chunkY + chunkZ * CHUNKS_Y) * World.CHUNKS_X].setDirty();
	}
	
	public void render(AbstractPixelRenderer3D renderer3d, float dt) {
		PlayerEntity player = world.getPlayer();

		float x = player.prevEyeX + (player.eyeX - player.prevEyeX) * dt;
		float y = player.prevEyeY + (player.eyeY - player.prevEyeY) * dt;
		float z = player.prevEyeZ + (player.eyeZ - player.prevEyeZ) * dt;
		camera.setPosition(-x, -y, -z);

		float yaw = player.prevYaw + (player.yaw - player.prevYaw) * dt;
		float pitch = player.prevPitch + (player.pitch - player.prevPitch) * dt;
		camera.setRotation(-yaw, -pitch);
		
		renderer3d.setShader(worldShader);

		ViewFrustum frustum = camera.getViewFrustum();
		
		Vec3f tmp = new Vec3f();
		for (ViewChunk chunk : chunks) {
			if (!chunk.isDirty() && chunk.getVertices().length == 0)
				continue;
				
			chunk.getCenter(tmp);
			
			if (frustum.sphereInView(tmp, CHUNK_SPHERE_RADIUS)) {
				if (chunk.isDirty())
					chunk.rebuild();
				
				Vertex3D[] vertices = chunk.getVertices();
				if (vertices.length != 0)
					renderer3d.drawVertices(vertices);
			}
		}
		
		selectionRenderer.render(renderer3d, dt);
	}

	public World getWorld() {
		return world;
	}
}
