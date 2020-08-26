package mineclone.client.renderer.world;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import mineclone.client.graphic.BufferAttrib;
import mineclone.client.graphic.BufferAttribType;
import mineclone.client.graphic.BufferDataType;
import mineclone.client.graphic.BufferLayout;
import mineclone.client.graphic.opengl.VertexArray;
import mineclone.client.graphic.opengl.VertexBuffer;
import mineclone.client.graphic.tessellator.Color;
import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.common.IResource;
import mineclone.common.math.ViewFrustum;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.WorldChunk;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.entity.PlayerEntity;

public class WorldRenderer implements IResource {

	private static final int CHUNKS_Y = IWorld.WORLD_HEIGHT / WorldChunk.CHUNK_SIZE;
	private static final int NUM_VIEW_CHUNKS = IWorld.CHUNKS_X * CHUNKS_Y * IWorld.CHUNKS_Z;
	
	private static final float CHUNK_SPHERE_RADIUS = (float)(Math.sqrt(3.0) * 0.5 * WorldChunk.CHUNK_SIZE);
	
	private static final float CAMERA_FOV = (float)Math.toRadians(70.0f);
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 1000.0f;
	
	public static final Color SKY_COLOR = Color.LIGHT_SKY_BLUE;

	private static final float FOG_DENSITY = 2.0f / (IWorld.CHUNKS_X * WorldChunk.CHUNK_SIZE);
	private static final float FOG_GRADIENT = 10.0f;
	public static final Color FOG_COLOR = SKY_COLOR;
	
	private final IClientWorld world;
	
	private final WorldCamera camera;
	private final WorldShader worldShader;
	
	private final BufferLayout bufferLayout;
	private final VertexArray vertexArray;
	private final int bufferBindingIndex;
	
	private final VertexAttribBuilder attribBuilder;
	
	private final ViewChunk[] chunks;
	private final int[] visibilityGraph;
	
	private final BlockSelectionRenderer selectionRenderer;
	
	public WorldRenderer(IClientWorld world) {
		this.world = world;
		
		camera = new WorldCamera();
		worldShader = new WorldShader();
		
		bufferLayout = new BufferLayout(
			new BufferAttrib("a_Position" , BufferAttribType.FLOAT3),
			new BufferAttrib("a_TexCoords", BufferAttribType.FLOAT2),
			new BufferAttrib("a_Color"    , BufferAttribType.FLOAT4, BufferDataType.UBYTE, true),
			new BufferAttrib("a_Lightness", BufferAttribType.FLOAT)
		);
		
		vertexArray = new VertexArray();
		bufferBindingIndex = vertexArray.prepareBufferBinding(bufferLayout);
		
		attribBuilder = new VertexAttribBuilder(bufferLayout);

		chunks = new ViewChunk[NUM_VIEW_CHUNKS];
		visibilityGraph = new int[NUM_VIEW_CHUNKS + 1];
		
		int index = 0;
		for (int chunkZ = 0; chunkZ < IWorld.CHUNKS_Z; chunkZ++) {
			for (int chunkY = 0; chunkY < CHUNKS_Y; chunkY++) {
				for (int chunkX = 0; chunkX < IWorld.CHUNKS_X; chunkX++) {
					chunks[index++] = new ViewChunk(this, chunkX, chunkY, chunkZ);
				}
			}
		}
		
		selectionRenderer = new BlockSelectionRenderer(world, camera);
	
		worldShader.setFogDensity(FOG_DENSITY);
		worldShader.setFogGradient(FOG_GRADIENT);
		worldShader.setFogColor(FOG_COLOR);
	}

	public void displaySizeChanged(int newWidth, int newHeight) {
		float aspect = (float)newWidth / newHeight;
		camera.setPerspective(CAMERA_FOV, aspect, CAMERA_NEAR, CAMERA_FAR);
	}

	public void markDirty(IBlockPosition pos, boolean includeBorders) {
		int chunkX = pos.getX() / ViewChunk.CHUNK_SIZE;
		int chunkY = pos.getY() / ViewChunk.CHUNK_SIZE;
		int chunkZ = pos.getZ() / ViewChunk.CHUNK_SIZE;

		markChunkDirty(chunkX, chunkY, chunkZ);
	
		if (includeBorders) {
			int xSub = Math.abs(pos.getX() % ViewChunk.CHUNK_SIZE);
			if (xSub == 0) {
				markChunkDirty(chunkX - 1, chunkY, chunkZ);
			} else if (xSub == ViewChunk.CHUNK_SIZE - 1) {
				markChunkDirty(chunkX + 1, chunkY, chunkZ);
			}
			
			int ySub = Math.abs(pos.getY() % ViewChunk.CHUNK_SIZE);
			if (ySub == 0) {
				markChunkDirty(chunkX, chunkY - 1, chunkZ);
			} else if (ySub == ViewChunk.CHUNK_SIZE - 1) {
				markChunkDirty(chunkX, chunkY + 1, chunkZ);
			}
			
			int zSub = Math.abs(pos.getZ() % ViewChunk.CHUNK_SIZE);
			if (zSub == 0) {
				markChunkDirty(chunkX, chunkY, chunkZ - 1);
			} else if (zSub == ViewChunk.CHUNK_SIZE - 1) {
				markChunkDirty(chunkX, chunkY, chunkZ + 1);
			}
		}
	}
	
	public void markRangeDirty(IBlockPosition p0, IBlockPosition p1, boolean includeBorders) {
		int x0 = Math.min(p0.getX(), p1.getX());
		int y0 = Math.min(p0.getY(), p1.getY());
		int z0 = Math.min(p0.getZ(), p1.getZ());

		int x1 = Math.max(p0.getX(), p1.getX());
		int y1 = Math.max(p0.getY(), p1.getY());
		int z1 = Math.max(p0.getZ(), p1.getZ());
		
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
		if (chunkX1 < 0 || chunkX0 >= IWorld.CHUNKS_X)
			return;
		if (chunkY1 < 0 || chunkY0 >= CHUNKS_Y)
			return;
		if (chunkZ1 < 0 || chunkZ0 >= IWorld.CHUNKS_Z)
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
		if (chunkX < 0 || chunkX >= IWorld.CHUNKS_X)
			return;
		if (chunkY < 0 || chunkY >= CHUNKS_Y)
			return;
		if (chunkZ < 0 || chunkZ >= IWorld.CHUNKS_Z)
			return;
		
		chunks[chunkX + (chunkY + chunkZ * CHUNKS_Y) * IWorld.CHUNKS_X].markDirty();
	}
	
	public void update() {
		selectionRenderer.update();
	}
	
	public void render(float dt) {
		updateCamera(dt);

		renderWorld(dt);
		
		selectionRenderer.render(dt);
	}

	private void updateCamera(float dt) {
		PlayerEntity player = world.getPlayer();

		float x = player.prevEyeX + (player.eyeX - player.prevEyeX) * dt;
		float y = player.prevEyeY + (player.eyeY - player.prevEyeY) * dt;
		float z = player.prevEyeZ + (player.eyeZ - player.prevEyeZ) * dt;

		camera.setView(-x, -y, -z, -player.getYaw(), -player.getPitch());
	}
	
	private void renderWorld(float dt) {
		buildVisibilityGraph();
		
		vertexArray.bind();
		worldShader.bind();
		
		worldShader.setCamera(camera);
		
		BlockTextures.bindBlocksTexture();
		
		renderVisibleChunks();
	}
	
	private void buildVisibilityGraph() {
		int p = 0;
		
		ViewFrustum frustum = camera.getViewFrustum();
		
		for (int i = 0; i < NUM_VIEW_CHUNKS; i++) {
			ViewChunk chunk = chunks[i];

			if (!chunk.isEmpty() || chunk.isDirty()) {
				if (frustum.sphereInView(chunk.getCenter(), CHUNK_SPHERE_RADIUS)) {
					if (chunk.isDirty())
						chunk.rebuildAll(attribBuilder);
					
					visibilityGraph[p++] = i;
				}
			}
		}
		
		visibilityGraph[p] = -1;
	}
	
	private void renderVisibleChunks() {
		for (int i = 0; visibilityGraph[i] >= 0; i++) {
			ViewChunk chunk = chunks[visibilityGraph[i]];
			
			if (!chunk.isEmpty()) {
				VertexBuffer buffer = chunk.getVertexBuffer();
				vertexArray.setBufferBinding(buffer, bufferBindingIndex);
				
				glDrawArrays(GL_QUADS, 0, chunk.getVertexCount());
			}
		}
	}
	
	public IClientWorld getWorld() {
		return world;
	}

	public BufferLayout getBufferLayout() {
		return bufferLayout;
	}
	
	@Override
	public void close() {
		vertexArray.close();
		attribBuilder.close();
		worldShader.close();
		
		for (ViewChunk chunk : chunks)
			chunk.close();
	}
}
