package minecraft.client.renderer.world;

import minecraft.client.graphic.opengl.VertexBuffer;
import minecraft.client.graphic.tessellator.VertexAttribBuilder;
import minecraft.client.renderer.model.IBlockModel;
import minecraft.common.IResource;
import minecraft.common.math.Vec3;
import minecraft.common.world.World;
import minecraft.common.world.WorldChunk;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.MutableBlockPosition;
import minecraft.common.world.block.state.BlockState;

public class ViewChunk implements IResource {

	public static final int CHUNK_SIZE = WorldChunk.CHUNK_SIZE;
	
	public static final int MAX_VERTEX_COUNT = 8192 * 4;
	
	private final WorldRenderer worldRenderer;
	
	private final int chunkX;
	private final int chunkY;
	private final int chunkZ;
	
	private final Vec3 center;

	private boolean dirty;
	
	private final VertexBuffer vertexBuffer;
	private int vertexCount;
	
	public ViewChunk(WorldRenderer worldRenderer, int chunkX, int chunkY, int chunkZ) {
		this.worldRenderer = worldRenderer;
		
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;

		float x = chunkX * CHUNK_SIZE;
		float y = chunkY * CHUNK_SIZE;
		float z = chunkZ * CHUNK_SIZE;
		
		center = new Vec3(x, y, z).add(CHUNK_SIZE * 0.5f);

		dirty = true;
	
		vertexBuffer = new VertexBuffer(worldRenderer.getBufferLayout(), MAX_VERTEX_COUNT);
		vertexCount = 0;
	}
	
	public void rebuildAll(VertexAttribBuilder builder) {
		World world = worldRenderer.getWorld();
		
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
						
						BlockState blockState = chunk.getBlockState(pos);
						Block block = blockState.getBlock();
						
						IBlockModel blockModel = block.getModel(world, pos, blockState);
						
						if (blockModel != null)
							blockModel.tessellate(world, pos, builder);
					}
				}
			}
	
			vertexBuffer.bufferSubData(builder.getReadbleBuffer(), 0);
			vertexCount = builder.getVertexCount();
			
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
	public void dispose() {
		vertexBuffer.close();
	}
}
