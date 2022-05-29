package mineclone.common.world.chunk;

import mineclone.common.world.block.state.IBlockState;

public interface IWorldChunk extends IChunk {

	public IBlockState getBlockState(int rx, int ry, int rz);

	public boolean setBlockState(int rx, int ry, int rz, IBlockState newState);

	public boolean hasRandomUpdates();
	
	public boolean isEmpty();
	
}
