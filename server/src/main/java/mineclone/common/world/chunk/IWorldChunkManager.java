package mineclone.common.world.chunk;

import java.util.Iterator;

import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public interface IWorldChunkManager {

	public IWorldChunk getChunk(IChunkPosition chunkPos);

	public boolean setChunk(IChunkPosition chunkPos, IWorldChunk chunk);
	
	public IBlockState getBlockState(IBlockPosition pos);

	public boolean setBlockState(IBlockPosition pos, IBlockState state);

	public int getHighestPoint(int x, int z);

	public boolean containsChunk(IChunkPosition chunkPos);

	default public boolean containsState(IBlockPosition pos) {
		return containsChunk(new ChunkPosition(pos));
	}
	
	public Iterator<ChunkEntry<IWorldChunk>> chunkIterator();

}
