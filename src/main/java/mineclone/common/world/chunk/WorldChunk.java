package mineclone.common.world.chunk;

import java.util.Arrays;

import mineclone.common.net.packet.PacketDecodeBuffer;
import mineclone.common.net.packet.PacketEncodeBuffer;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.state.IBlockState;

public class WorldChunk implements IWorldChunk {

	private final IBlockState[] states;
	private int randomUpdateCount;

	public WorldChunk() {
		states = new IBlockState[CHUNK_VOLUME];
		randomUpdateCount = 0;

		Arrays.fill(states, Blocks.AIR_BLOCK.getDefaultState());
	}
	
	public WorldChunk(IWorldChunk other) {
		states = new IBlockState[CHUNK_VOLUME];

		if (other instanceof WorldChunk) {
			WorldChunk otherWorldChunk = (WorldChunk)other;
			for (int i = 0; i < CHUNK_VOLUME; i++)
				states[i] = otherWorldChunk.states[i];
			randomUpdateCount = otherWorldChunk.randomUpdateCount;
		} else {
			randomUpdateCount = 0;
			
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

	public static void write(PacketEncodeBuffer buffer, WorldChunk chunk) {
		for (int i = 0; i < CHUNK_VOLUME; i++) {
			IBlockState state = chunk.states[i];
			buffer.writeInt(Blocks.getIdentifier(state));
		}
	}
	
	public static WorldChunk read(PacketDecodeBuffer buffer) {
		WorldChunk chunk = new WorldChunk();
		for (int i = 0; i < CHUNK_VOLUME; i++) {
			IBlockState state = Blocks.getState(buffer.readInt());
			chunk.states[i] = (state == null) ? Blocks.AIR_BLOCK.getDefaultState() : state;
		}
		
		return chunk;
	}
}
