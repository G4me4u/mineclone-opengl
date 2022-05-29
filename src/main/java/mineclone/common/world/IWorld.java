package mineclone.common.world;

import java.util.List;

import mineclone.common.math.Vec3;
import mineclone.common.world.block.Block;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunkManager;

public interface IWorld {
	
	public static final int CHUNKS_X = 16;
	public static final int CHUNKS_Y = 8;
	public static final int CHUNKS_Z = 16;
	
	public BlockHitResult castBlockRay(float x, float y, float z, Vec3 dir);
	
	public IBlockState getBlockState(IBlockPosition pos);
	
	public boolean setBlockState(IBlockPosition pos, IBlockState state);
	
	default public Block getBlock(IBlockPosition pos) {
		return getBlockState(pos).getBlock();
	}

	default public boolean setBlock(IBlockPosition pos, Block block) {
		return setBlockState(pos, block.getDefaultState());
	}
	
	public int getHighestPoint(IBlockPosition pos);
	
	public boolean isLoadedBlock(IBlockPosition pos);
	
	public boolean isLoadedChunk(IChunkPosition chunkPos);
	
	public void update();
	
	public List<EntityHitbox> getBlockHitboxes(EntityHitbox hitbox);
	
	public IWorldChunkManager getChunkManager();
	
}
