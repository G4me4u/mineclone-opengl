package minecraft.renderer.world;

import minecraft.IResource;
import minecraft.graphic.opengl.buffer.VertexBuffer;
import minecraft.graphic.tessellator.VertexAttribBuilder;
import minecraft.math.Vec3;
import minecraft.renderer.model.IBlockModel;
import minecraft.world.Blocks;
import minecraft.world.World;
import minecraft.world.WorldChunk;
import minecraft.world.block.Block;
import minecraft.world.block.MutableBlockPosition;
import minecraft.world.block.state.BlockState;

public class ViewChunk implements IResource {

	public static final int CHUNK_SIZE = WorldChunk.CHUNK_SIZE;
	
	public static final int MAX_VERTEX_COUNT = 16384;
	
	private final WorldRenderer worldRenderer;
	
	private final int chunkX;
	private final int chunkY;
	private final int chunkZ;
	
	private final Vec3 center;

	private boolean dirty;
	private boolean allEmpty;
	
	private final VertexBuffer[] layerBuffers;
	private final int[] vertexCounts;
	
	public ViewChunk(WorldRenderer worldRenderer, int chunkX, int chunkY, int chunkZ) {
		this.worldRenderer = worldRenderer;
		
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;

		float x = chunkX * CHUNK_SIZE;
		float y = chunkY * CHUNK_SIZE;
		float z = chunkZ * CHUNK_SIZE;
		
		center = new Vec3(x, y, z).add(CHUNK_SIZE * 0.5f);
	
		dirty = allEmpty = true;

		layerBuffers = new VertexBuffer[RenderLayer.LAYERS.length];
		for (int i = 0; i < layerBuffers.length; i++)
			layerBuffers[i] = new VertexBuffer(worldRenderer.getBufferLayout(), MAX_VERTEX_COUNT);

		vertexCounts = new int[RenderLayer.LAYERS.length];
	}
	
	public void rebuildAll(VertexAttribBuilder builder) {
		allEmpty = true;
		
		for (RenderLayer layer : RenderLayer.LAYERS)
			allEmpty &= !rebuildLayer(builder, layer);
	
		dirty = false;
	}
	
	private boolean rebuildLayer(VertexAttribBuilder builder, RenderLayer layer) {
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
						
						if (block != Blocks.AIR_BLOCK && block.getLayer(world, pos, blockState) == layer) {
							IBlockModel blockModel = block.getModel(world, pos, blockState);
							if (blockModel != null)
								blockModel.tessellate(world, pos, builder);
						}
					}
				}
			}
	
			VertexBuffer buffer = layerBuffers[layer.getIndex()];
			buffer.bufferSubData(builder.getReadbleBuffer(), 0);

			int vertexCount = builder.getVertexCount();
			vertexCounts[layer.getIndex()] = vertexCount;
			
			builder.clear();
			
			return (vertexCount != 0);
		}
		
		return false;
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
	
	public VertexBuffer getVertexBuffer(RenderLayer layer) {
		return layerBuffers[layer.getIndex()];
	}
	
	public int getVertexCount(RenderLayer layer) {
		return vertexCounts[layer.getIndex()];
	}

	public boolean isAllEmpty() {
		return allEmpty;
	}
	
	public void markDirty() {
		dirty = true;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public Vec3 getCenter() {
		return center;
	}
	
	@Override
	public void dispose() {
		for (VertexBuffer buffer : layerBuffers)
			buffer.close();
	}
}
