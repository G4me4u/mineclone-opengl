package mineclone.common.world.chunk;

import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.state.IBlockState;

public final class EmptyWorldChunk implements IWorldChunk {

	public static final EmptyWorldChunk INSTANCE = new EmptyWorldChunk();
	
	private EmptyWorldChunk() {
	}
	
	@Override
	public IBlockState getBlockState(int rx, int ry, int rz) {
		return Blocks.AIR_BLOCK.getDefaultState();
	}

	@Override
	public boolean setBlockState(int rx, int ry, int rz, IBlockState newState) {
		return false;
	}
	
	@Override
	public boolean hasRandomUpdates() {
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}
}
