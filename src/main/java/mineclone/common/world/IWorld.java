package mineclone.common.world;

import java.util.List;

import mineclone.common.math.Vec3;
import mineclone.common.world.block.Block;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public interface IWorld {
	
	public static final int WORLD_HEIGHT = WorldChunk.CHUNK_SIZE * 8;
	
	public static final int CHUNKS_X = 16;
	public static final int CHUNKS_Z = 16;
	
	public BlockHitResult castBlockRay(float x, float y, float z, Vec3 dir);
	
	public WorldChunk getChunk(IBlockPosition pos);

	public WorldChunk getChunk(int chunkX, int chunkZ);
	
	public IBlockState getBlockState(IBlockPosition pos);
	
	public Block getBlock(IBlockPosition pos);
	
	public int getHighestPoint(IBlockPosition pos);
	
	public boolean isLoadedBlock(IBlockPosition pos);
	
	public boolean isLoadedBlock(int chunkX, int chunkZ);
	
	public void update();
	
	public int getHeight();
	
	public List<EntityHitbox> getBlockHitboxes(EntityHitbox hitbox);
	
}
