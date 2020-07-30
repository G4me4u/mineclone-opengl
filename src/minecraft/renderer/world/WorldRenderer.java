package minecraft.renderer.world;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;

import minecraft.IResource;
import minecraft.graphic.opengl.buffer.BufferAttrib;
import minecraft.graphic.opengl.buffer.BufferAttribType;
import minecraft.graphic.opengl.buffer.BufferLayout;
import minecraft.graphic.opengl.buffer.VertexArray;
import minecraft.graphic.opengl.buffer.VertexBuffer;
import minecraft.graphic.tessellator.VertexAttribBuilder;
import minecraft.math.ViewFrustum;
import minecraft.world.World;
import minecraft.world.WorldChunk;
import minecraft.world.block.IBlockPosition;
import minecraft.world.entity.PlayerEntity;

public class WorldRenderer implements IResource {

	private static final int CHUNKS_Y = World.WORLD_HEIGHT / WorldChunk.CHUNK_SIZE;
	private static final int NUM_VIEW_CHUNKS = World.CHUNKS_X * CHUNKS_Y * World.CHUNKS_Z;
	
	private static final float CHUNK_SPHERE_RADIUS = (float)(Math.sqrt(3.0) * 0.5 * WorldChunk.CHUNK_SIZE);
	
	private static final float CAMERA_FOV = 70.0f;
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 1000.0f;
	
	private final World world;
	
	private final WorldCamera camera;
	private final WorldShader worldShader;
	
	private final BufferLayout bufferLayout;
	private final VertexArray vertexArray;
	private final int bufferBindingIndex;
	
	private final VertexAttribBuilder attribBuilder;
	
	private final ViewChunk[] chunks;
	private final int[] visibilityGraph;
	
	private final BlockSelectionRenderer selectionRenderer;
	
	public WorldRenderer(World world) {
		this.world = world;
		
		camera = new WorldCamera();
		worldShader = new WorldShader();
		
		bufferLayout = new BufferLayout(
			new BufferAttrib("a_Position", BufferAttribType.FLOAT3),
			new BufferAttrib("a_TexCoords", BufferAttribType.FLOAT2),
			new BufferAttrib("a_Lightness", BufferAttribType.FLOAT)
		);
		
		vertexArray = new VertexArray();
		bufferBindingIndex = vertexArray.prepareBufferBinding(bufferLayout);
		
		attribBuilder = new VertexAttribBuilder(bufferLayout);

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

	public void displaySizeChanged(int newWidth, int newHeight) {
		float aspect = (float)newWidth / newHeight;
		camera.setPerspective(CAMERA_FOV, aspect, CAMERA_NEAR, CAMERA_FAR);
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

		camera.setPosition(-x, -y, -z);
		camera.setRotation(-player.yaw, -player.pitch);
	}
	
	private void renderWorld(float dt) {
		buildVisibilityGraph();
		
		vertexArray.bind();
		worldShader.bind();
		
		worldShader.setProjViewMat(camera);
		
		BlockTextures.bindBlocksTexture();
		
		for (RenderLayer layer : RenderLayer.LAYERS)
			renderWorldLayer(layer);
	}
	
	private void buildVisibilityGraph() {
		int p = 0;
		
		ViewFrustum frustum = camera.getViewFrustum();
		
		for (int i = 0; i < NUM_VIEW_CHUNKS; i++) {
			ViewChunk chunk = chunks[i];

			if (!chunk.isDirty() && chunk.isAllEmpty())
				continue;
			
			if (frustum.sphereInView(chunk.getCenter(), CHUNK_SPHERE_RADIUS)) {
				if (chunk.isDirty())
					chunk.rebuildAll(attribBuilder);
		
				visibilityGraph[p++] = i;
			}
		}
		
		visibilityGraph[p] = -1;
	}
	
	private void renderWorldLayer(RenderLayer layer) {
		if (layer == RenderLayer.SPRITE_LAYER)
			glDisable(GL_CULL_FACE);

		for (int i = 0; visibilityGraph[i] >= 0; i++) {
			ViewChunk chunk = chunks[visibilityGraph[i]];
			
			int count = chunk.getVertexCount(layer);

			if (count > 0) {
				VertexBuffer buffer = chunk.getVertexBuffer(layer);
				vertexArray.setBufferBinding(buffer, bufferBindingIndex);
				
				glDrawArrays(GL_QUADS, 0, count);
			}
		}
		
		glEnable(GL_CULL_FACE);
	}
	
	public World getWorld() {
		return world;
	}

	public BufferLayout getBufferLayout() {
		return bufferLayout;
	}
	
	@Override
	public void dispose() {
		vertexArray.close();
		attribBuilder.close();
		worldShader.close();
		
		for (ViewChunk chunk : chunks)
			chunk.close();
	}
}
