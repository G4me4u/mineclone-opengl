package com.g4mesoft.minecraft.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.g4mesoft.graphics3d.AbstractPixelRenderer3D;
import com.g4mesoft.graphics3d.IVertexProvider;
import com.g4mesoft.graphics3d.Texture3D;
import com.g4mesoft.graphics3d.VertexTessellator3D;
import com.g4mesoft.graphics3d.ViewFrustum3D;
import com.g4mesoft.math.MathUtils;
import com.g4mesoft.minecraft.renderer.tessellator.ViewChunk;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.WorldChunk;
import com.g4mesoft.minecraft.world.block.IBlockPosition;
import com.g4mesoft.minecraft.world.entity.PlayerEntity;
import com.g4mesoft.util.FileUtil;

public class WorldRenderer {

	private static final int CHUNKS_Y = World.WORLD_HEIGHT / WorldChunk.CHUNK_SIZE;
	private static final int NUM_VIEW_CHUNKS = World.CHUNKS_X * CHUNKS_Y * World.CHUNKS_Z;
	
	private static final float CHUNK_SPHERE_RADIUS = MathUtils.sqrt(3.0f) * 0.5f * WorldChunk.CHUNK_SIZE;
	
	private static final float FOV = 70.0f;
	private static final float NEAR = 0.1f;
	private static final float FAR = 1000.0f;
	
	public static final int NUM_VERTEX_DATA = 3;
	
	public static final int LOCATION_TEX_UV = 0;
	public static final int LOCATION_TEX_U = LOCATION_TEX_UV + 0;
	public static final int LOCATION_TEX_V = LOCATION_TEX_UV + 1;
	public static final int LOCATION_LIGHTNESS = 2;
	
	private static final String TEXTURE_FILE = "/assets/textures/blocks.png";
	
	private final World world;
	
	private final WorldCamera camera;
	private final WorldShader3D worldShader;
	private final VertexTessellator3D tessellator;
	
	private final ViewChunk[] chunks;
	private final int[] visibilityGraph;
	
	private final BlockSelectionRenderer selectionRenderer;
	
	public WorldRenderer(World world) {
		this.world = world;
		
		camera = new WorldCamera();
		worldShader = new WorldShader3D(camera, loadTexture(TEXTURE_FILE));
		tessellator = new VertexTessellator3D(NUM_VERTEX_DATA);
		
		chunks = new ViewChunk[NUM_VIEW_CHUNKS];
		visibilityGraph = new int[NUM_VIEW_CHUNKS + 1];
		
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
	
	private Texture3D loadTexture(String textureFile) {
		InputStream is = FileUtil.getInputStream(textureFile, true);
		if (is == null)
			throw new NullPointerException("Texture file " + textureFile + " not found!");

		BufferedImage image = null;
		try {
			image = ImageIO.read(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (image == null)
			throw new RuntimeException("Unable to load texture " + textureFile);
	
		return new Texture3D(image);
	}

	public void displayResized(int newWidth, int newHeight) {
		float aspect = (float)newWidth / newHeight;
		camera.setPerspective(FOV, aspect, NEAR, FAR);
	}

	public void markDirty(IBlockPosition blockPos, boolean includeBorders) {
		int chunkX = blockPos.getX() / ViewChunk.CHUNK_SIZE;
		int chunkY = blockPos.getY() / ViewChunk.CHUNK_SIZE;
		int chunkZ = blockPos.getZ() / ViewChunk.CHUNK_SIZE;

		markChunkDirty(chunkX, chunkY, chunkZ);
	
		if (includeBorders) {
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
	}
	
	public void markRangeDirty(IBlockPosition p0, IBlockPosition p1, boolean includeBorders) {
		int x0 = MathUtils.min(p0.getX(), p1.getX());
		int y0 = MathUtils.min(p0.getY(), p1.getY());
		int z0 = MathUtils.min(p0.getZ(), p1.getZ());

		int x1 = MathUtils.max(p0.getX(), p1.getX());
		int y1 = MathUtils.max(p0.getY(), p1.getY());
		int z1 = MathUtils.max(p0.getZ(), p1.getZ());
		
		int chunkX0 = x0 / ViewChunk.CHUNK_SIZE;
		int chunkX1 = x1 / ViewChunk.CHUNK_SIZE;

		int chunkY0 = y0 / ViewChunk.CHUNK_SIZE;
		int chunkY1 = y1 / ViewChunk.CHUNK_SIZE;

		int chunkZ0 = z0 / ViewChunk.CHUNK_SIZE;
		int chunkZ1 = z1 / ViewChunk.CHUNK_SIZE;

		markChunksDirty(chunkX0, chunkY0, chunkZ0, chunkX1, chunkY1, chunkZ1);
		
		if (includeBorders) {
			if (x0 % ViewChunk.CHUNK_SIZE == 0)
				markChunksDirty(chunkX0 - 1, chunkY0, chunkZ0, chunkX0 - 1, chunkY1, chunkZ1);
			if (x1 % ViewChunk.CHUNK_SIZE == ViewChunk.CHUNK_SIZE - 1)
				markChunksDirty(chunkX1 + 1, chunkY0, chunkZ0, chunkX1 + 1, chunkY1, chunkZ1);
			
			if (y0 % ViewChunk.CHUNK_SIZE == 0)
				markChunksDirty(chunkX0, chunkY0 - 1, chunkZ0, chunkX1, chunkY0 - 1, chunkZ1);
			if (y1 % ViewChunk.CHUNK_SIZE == ViewChunk.CHUNK_SIZE - 1)
				markChunksDirty(chunkX0, chunkY1 + 1, chunkZ0, chunkX1, chunkY1 + 1, chunkZ1);
	
			if (z0 % ViewChunk.CHUNK_SIZE == 0)
				markChunksDirty(chunkX0, chunkY0, chunkZ0 - 1, chunkX1, chunkY1, chunkZ0 - 1);
			if (z1 % ViewChunk.CHUNK_SIZE == ViewChunk.CHUNK_SIZE - 1)
				markChunksDirty(chunkX0, chunkY0, chunkZ1 + 1, chunkX1, chunkY1, chunkZ1 + 1);
		}
	}

	public void markChunksDirty(int chunkX0, int chunkY0, int chunkZ0, int chunkX1, int chunkY1, int chunkZ1) {
		if (chunkX1 < 0 || chunkX0 >= World.CHUNKS_X)
			return;
		if (chunkY1 < 0 || chunkY0 >= CHUNKS_Y)
			return;
		if (chunkZ1 < 0 || chunkZ0 >= World.CHUNKS_Z)
			return;
		
		for (int chunkX = chunkX0; chunkX <= chunkX1; chunkX++) {
			for (int chunkY = chunkY0; chunkY <= chunkY1; chunkY++) {
				for (int chunkZ = chunkZ0; chunkZ <= chunkZ1; chunkZ++) {
					markChunkDirty(chunkX, chunkY, chunkZ);
				}
			}
		}
	}
	
	public void markChunkDirty(int chunkX, int chunkY, int chunkZ) {
		if (chunkX < 0 || chunkX >= World.CHUNKS_X)
			return;
		if (chunkY < 0 || chunkY >= CHUNKS_Y)
			return;
		if (chunkZ < 0 || chunkZ >= World.CHUNKS_Z)
			return;
		
		chunks[chunkX + (chunkY + chunkZ * CHUNKS_Y) * World.CHUNKS_X].markDirty();
	}
	
	public void update() {
		selectionRenderer.update();
	}
	
	public void render(AbstractPixelRenderer3D renderer3d, float dt) {
		updateCamera(dt);

		renderWorld(renderer3d, dt);
		
		selectionRenderer.render(renderer3d, dt);
	}

	private void updateCamera(float dt) {
		PlayerEntity player = world.getPlayer();

		float x = player.prevEyeX + (player.eyeX - player.prevEyeX) * dt;
		float y = player.prevEyeY + (player.eyeY - player.prevEyeY) * dt;
		float z = player.prevEyeZ + (player.eyeZ - player.prevEyeZ) * dt;
		camera.setPosition(-x, -y, -z);

		float yaw = player.prevYaw + (player.yaw - player.prevYaw) * dt;
		float pitch = player.prevPitch + (player.pitch - player.prevPitch) * dt;
		camera.setRotation(-yaw, -pitch);
	}
	
	private void renderWorld(AbstractPixelRenderer3D renderer3d, float dt) {
		buildVisibilityGraph();
		
		renderer3d.setShader(worldShader);

		for (RenderLayer layer : RenderLayer.LAYERS)
			renderWorldLayer(renderer3d, layer);
	}
	
	private void buildVisibilityGraph() {
		int p = 0;
		
		ViewFrustum3D frustum = camera.getViewFrustum();
		
		for (int i = 0; i < NUM_VIEW_CHUNKS; i++) {
			ViewChunk chunk = chunks[i];
			
			if (!chunk.isDirty() && chunk.isAllEmpty())
				continue;
			
			if (frustum.sphereInView(chunk.getCenter(), CHUNK_SPHERE_RADIUS)) {
				if (chunk.isDirty())
					chunk.rebuildAll(tessellator);
		
				visibilityGraph[p++] = i;
			}
		}
		
		visibilityGraph[p] = -1;
	}
	
	private void renderWorldLayer(AbstractPixelRenderer3D renderer3d, RenderLayer layer) {
		renderer3d.setCullEnabled(layer != RenderLayer.SPRITE_LAYER);
		
		for (int i = 0; visibilityGraph[i] >= 0; i++) {
			ViewChunk chunk = chunks[visibilityGraph[i]];

			if (!chunk.isEmpty(layer)) {
				IVertexProvider vertices = chunk.getVertices(layer);
			
				if (vertices != null)
					renderer3d.drawVertices(vertices);
			}
		}
		
		renderer3d.setCullEnabled(true);
	}
	
	public World getWorld() {
		return world;
	}
}
