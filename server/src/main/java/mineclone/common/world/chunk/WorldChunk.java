package mineclone.common.world.chunk;

import java.util.Arrays;

import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.state.IBlockState;

public class WorldChunk implements IWorldChunk {

	private final IBlockState[] states;
	private int randomTickingCount;
	private int airStateCount;

	public WorldChunk() {
		states = new IBlockState[CHUNK_VOLUME];
		randomTickingCount = 0;
		airStateCount = CHUNK_VOLUME;
	
		Arrays.fill(states, Blocks.AIR_BLOCK.getDefaultState());
	}
	
	public WorldChunk(IWorldChunk other) {
		states = new IBlockState[CHUNK_VOLUME];

		if (other instanceof WorldChunk) {
			WorldChunk otherWorldChunk = (WorldChunk)other;
			for (int i = 0; i < CHUNK_VOLUME; i++)
				states[i] = otherWorldChunk.states[i];
			randomTickingCount = otherWorldChunk.randomTickingCount;
			airStateCount = otherWorldChunk.airStateCount;
		} else {
			randomTickingCount = 0;
			airStateCount = CHUNK_VOLUME;

			Arrays.fill(states, Blocks.AIR_BLOCK.getDefaultState());
			
			for (int rz = 0; rz < CHUNK_SIZE; rz++) {
				for (int ry = 0; ry < CHUNK_SIZE; ry++) {
					for (int rx = 0; rx < CHUNK_SIZE; rx++) {
						IBlockState state = other.getBlockState(rx, ry, rz);
						setBlockState(rx, ry, rz, state);
					}
				}
			}
		}
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
			
			if (oldState.doesRandomTicks())
				randomTickingCount--;
			if (newState.doesRandomTicks())
				randomTickingCount++;

			if (oldState.isAir())
				airStateCount--;
			if (newState.isAir())
				airStateCount++;
			
			return true;
		}

		return false;
	}

	@Override
	public boolean hasRandomTickingBlocks() {
		return (randomTickingCount > 0);
	}

	@Override
	public boolean isEmpty() {
		return (airStateCount == CHUNK_VOLUME);
	}
}
