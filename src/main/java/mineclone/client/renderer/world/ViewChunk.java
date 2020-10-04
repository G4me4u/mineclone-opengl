package mineclone.client.renderer.world;

import mineclone.client.graphic.opengl.VertexBuffer;
import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.client.renderer.model.IBlockModel;
import mineclone.common.IResource;
import mineclone.common.math.Vec3;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.WorldChunk;
import mineclone.common.world.block.MutableBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class ViewChunk implements IResource {

	public static final int CHUNK_SIZE = WorldChunk.CHUNK_SIZE;
	
	private final IClientWorld world;
	private final VertexBufferPool bufferPool;
	
	private final int chunkX;
	private final int chunkY;
	private final int chunkZ;
	
	private final Vec3 center;

	private boolean dirty;
	
	private VertexBuffer vertexBuffer;
	private int vertexCount;
	
	public ViewChunk(IClientWorld world, VertexBufferPool bufferPool, int chunkX, int chunkY, int chunkZ) {
		this.world = world;
		this.bufferPool = bufferPool;
		
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;

		float x = chunkX * CHUNK_SIZE;
		float y = chunkY * CHUNK_SIZE;
		float z = chunkZ * CHUNK_SIZE;
		
		center = new Vec3(x, y, z).add(CHUNK_SIZE * 0.5f);

		dirty = true;
	
		vertexBuffer = null;
		vertexCount = 0;
	}
	
	public void rebuildAll(VertexAttribBuilder builder) {
		int xc = getX();
		int yc = getY();
		int zc = getZ();
		
		MutableBlockPosition pos = new MutableBlockPosition(xc, yc, zc);
		WorldChunk chunk = world.getChunk(pos);
		
		if (chunk != null) {
			for (int zo = 0; zo < CHUNK_SIZE; zo++) {
				for (int yo = 0; yo < CHUNK_SIZE; yo++) {
					for (int xo = 0; xo < CHUNK_SIZE; xo++) {
						pos.x = xc + xo;
						pos.y = yc + yo;
						pos.z = zc + zo;
						
						IBlockState state = chunk.getBlockState(pos);
						IBlockModel blockModel = state.getModel(world, pos);
						
						if (blockModel != null)
							blockModel.tessellate(world, pos, state, builder);
					}
				}
			}
	
			if (vertexBuffer != null)
				bufferPool.free(vertexBuffer);
			
			vertexCount = builder.getVertexCount();
			
			if (vertexCount != 0) {
				vertexBuffer = bufferPool.aquire(vertexCount);
				vertexBuffer.bufferSubData(builder.getReadableBuffer(), 0);
			} else {
				vertexBuffer = null;
			}

			builder.clear();
		} else {
			vertexCount = 0;
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

	public void markDirty() {
		dirty = true;
	}
	
	public boolean isDirty() {
		return dirty;
	}

	public VertexBuffer getVertexBuffer() {
		return vertexBuffer;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}

	public boolean isEmpty() {
		return (vertexCount == 0);
	}
	
	public Vec3 getCenter() {
		return center;
	}
	
	@Override
	public void close() {
		if (vertexBuffer != null) {
			bufferPool.free(vertexBuffer);
			vertexBuffer = null;
		}
	}
}
