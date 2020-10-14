package mineclone.common.world.chunk;

import java.util.Arrays;

import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.state.IBlockState;

public class WorldChunk implements IWorldChunk {

	private static final int STATE_COUNT = CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE;
	
	private final IBlockState[] states;
	private int randomUpdateCount;

	public WorldChunk() {
		states = new IBlockState[STATE_COUNT];
		randomUpdateCount = 0;

		Arrays.fill(states, Blocks.AIR_BLOCK.getDefaultState());
	}
	
	private int getStateIndex(int rx, int ry, int rz) {
		return (rx + (ry + (rz << CHUNK_SHIFT) << CHUNK_SHIFT));
	}
	
	@Override
	public IBlockState getBlockState(int rx, int ry, int rz) {
		return states[getStateIndex(rx, ry, rz)];
	}

	@Override
	public boolean setBlockState(int rx, int ry, int rz, IBlockState newState) {
		int index = getStateIndex(rx, ry, rz);
		
		IBlockState oldState = states[index];
		
		if (newState != oldState) {
			states[index] = newState;
			
			if (oldState.hasRandomUpdate())
				randomUpdateCount--;
			if (newState.hasRandomUpdate())
				randomUpdateCount++;

			return true;
		}

		return false;
	}

	@Override
	public boolean hasRandomUpdates() {
		return (randomUpdateCount > 0);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
