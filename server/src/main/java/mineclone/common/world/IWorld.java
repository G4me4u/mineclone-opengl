package mineclone.common.world;

import java.util.List;

import mineclone.common.math.Vec3;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunkManager;

public interface IWorld {

	public static final int CHUNKS_X = 16;
	public static final int CHUNKS_Y = 8;
	public static final int CHUNKS_Z = 16;

	BlockHitResult castBlockRay(float x, float y, float z, Vec3 dir);

	IBlockState getBlockState(IBlockPosition pos);

	boolean setBlockState(IBlockPosition pos, IBlockState state);

	default IBlock getBlock(IBlockPosition pos) {
		return getBlockState(pos).getBlock();
	}

	default boolean setBlock(IBlockPosition pos, IBlock block) {
		return setBlockState(pos, block.getDefaultState());
	}

	int getHighestPoint(IBlockPosition pos);

	boolean isLoadedBlock(IBlockPosition pos);

	boolean isLoadedChunk(IChunkPosition chunkPos);

	void update();

	List<EntityHitbox> getBlockHitboxes(EntityHitbox hitbox);

	IWorldChunkManager getChunkManager();

}
