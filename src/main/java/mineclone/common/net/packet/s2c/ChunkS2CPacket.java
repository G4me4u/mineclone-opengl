package mineclone.common.net.packet.s2c;

import java.util.Arrays;

import mineclone.common.net.handler.IClientPacketHandler;
import mineclone.common.net.packet.IPacket;
import mineclone.common.net.packet.PacketDecodeBuffer;
import mineclone.common.net.packet.PacketEncodeBuffer;
import mineclone.common.util.DebugUtil;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.IWorldChunk;

public class ChunkS2CPacket implements IPacket<IClientPacketHandler> {

	private static final int CHUNK_SIZE = IWorldChunk.CHUNK_SIZE;
	private static final int CHUNK_STATE_COUNT = CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE;
	
	private int chunkX;
	private int chunkY;
	private int chunkZ;
	
	private IBlockState[] states;
	
	public ChunkS2CPacket() {
	}

	public ChunkS2CPacket(int chunkX, int chunkY, int chunkZ, IBlockState[] states) {
		if (DebugUtil.PERFORM_CHECKS) {
			if (states.length != CHUNK_STATE_COUNT)
				throw new IllegalArgumentException("The states array be an entire chunk!");
		}
		
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;
		
		this.states = Arrays.copyOf(states, states.length);
	}
	
	@Override
	public void encode(PacketEncodeBuffer buffer) {
		buffer.writeInt(chunkX);
		buffer.writeInt(chunkY);
		buffer.writeInt(chunkZ);
		
		for (IBlockState state : states)
			buffer.writeInt(Blocks.getIdentifier(state));
	}

	@Override
	public void decode(PacketDecodeBuffer buffer) {
		chunkX = buffer.readInt();
		chunkY = buffer.readInt();
		chunkZ = buffer.readInt();

		states = new IBlockState[CHUNK_STATE_COUNT];
		
		int i = 0;
		for (int z = 0; z < CHUNK_SIZE; z++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				for (int x = 0; x < CHUNK_SIZE; x++) {
					IBlockState state = Blocks.getState(buffer.readInt());
					states[i++] = (state == null) ? Blocks.AIR_BLOCK.getDefaultState() : state;
				}
			}
		}
	}

	@Override
	public void handle(IClientPacketHandler handler) {
		handler.onWorldChunk(this);
	}
	
	public int getChunkX() {
		return chunkX;
	}

	public int getChunkY() {
		return chunkY;
	}

	public int getChunkZ() {
		return chunkZ;
	}
	
	public IBlockState[] getStates() {
		return states;
	}
}
