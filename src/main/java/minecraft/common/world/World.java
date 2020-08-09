package minecraft.common.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import minecraft.common.math.Vec3;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.MutableBlockPosition;
import minecraft.common.world.block.state.IBlockState;

public class World implements IWorld {
	
	public static final int WORLD_HEIGHT = WorldChunk.CHUNK_SIZE * 8;
	
	public static final int CHUNKS_X = 16;
	public static final int CHUNKS_Z = 16;
	
	protected static final int RANDOM_TICK_SPEED = 3;
	
	protected final WorldChunk[] chunks;
	protected final Random random;
	
	protected BlockRay blockRay;
	
	public World() {
		chunks = new WorldChunk[CHUNKS_X * CHUNKS_Z];
		random = new Random();

		blockRay = new BlockRay(this, 0.01f);
	}
	
	@Override
	public BlockHitResult castBlockRay(float x, float y, float z, Vec3 dir) {
		return blockRay.castRay(x, y, z, dir);
	}
	
	public WorldChunk getChunk(IBlockPosition pos) {
		if (pos.getY() < 0 || pos.getY() >= WORLD_HEIGHT)
			return null;
		
		int chunkX = Math.floorDiv(pos.getX(), WorldChunk.CHUNK_SIZE);
		int chunkZ = Math.floorDiv(pos.getZ(), WorldChunk.CHUNK_SIZE);
		
		return getChunk(chunkX, chunkZ);
	}

	public WorldChunk getChunk(int chunkX, int chunkZ) {
		if (chunkX < 0 || chunkX >= CHUNKS_X)
			return null;
		if (chunkZ < 0 || chunkZ >= CHUNKS_Z)
			return null;
		
		return chunks[chunkX + chunkZ * CHUNKS_X];
	}
	
	@Override
	public IBlockState getBlockState(IBlockPosition pos) {
		WorldChunk chunk = getChunk(pos);
		if (chunk == null)
			return Blocks.AIR_BLOCK.getDefaultState();
		return chunk.getBlockState(pos);
	}
	
	@Override
	public Block getBlock(IBlockPosition pos) {
		return getBlockState(pos).getBlock();
	}
	
	@Override
	public int getHighestPoint(IBlockPosition pos) {
		WorldChunk chunk = getChunk(pos);
		if (chunk == null)
			return 0;
		return chunk.getHighestPoint(pos);
	}
	
	@Override
	public boolean isLoadedBlock(IBlockPosition pos) {
		return getChunk(pos) != null;
	}
	
	@Override
	public boolean isLoadedBlock(int chunkX, int chunkZ) {
		return getChunk(chunkX, chunkZ) != null;
	}
	
	@Override
	public void update() {
	}
	
	@Override
	public int getHeight() {
		return WORLD_HEIGHT;
	}
	
	@Override
	public List<EntityHitbox> getBlockHitboxes(EntityHitbox hitbox) {
		List<EntityHitbox> hitboxes = new ArrayList<>();
		
		MutableBlockPosition pos = new MutableBlockPosition();
		
		int x1 = (int)hitbox.x1;
		int y1 = (int)hitbox.y1;
		int z1 = (int)hitbox.z1;
		
		for (pos.x = (int)hitbox.x0; pos.x <= x1; pos.x++) {
			for (pos.y = (int)hitbox.y0; pos.y <= y1; pos.y++) {
				for (pos.z = (int)hitbox.z0; pos.z <= z1; pos.z++)
					getBlockState(pos).getEntityHitboxes(this, pos, hitboxes);
			}
		}
		
		return hitboxes;
	}
}
