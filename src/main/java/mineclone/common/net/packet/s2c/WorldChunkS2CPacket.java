package mineclone.common.net.packet.s2c;

import java.util.Arrays;

import mineclone.common.net.handler.IClientPacketHandler;
import mineclone.common.net.packet.IPacket;
import mineclone.common.net.packet.PacketDecodeBuffer;
import mineclone.common.net.packet.PacketEncodeBuffer;
import mineclone.common.util.DebugUtil;
import mineclone.common.world.World;
import mineclone.common.world.WorldChunk;
import mineclone.common.world.block.state.IBlockState;

public class WorldChunkS2CPacket implements IPacket<IClientPacketHandler> {

	private static final int CHUNK_SIZE = WorldChunk.CHUNK_SIZE;
	private static final int CHUNK_HEIGHT = World.WORLD_HEIGHT;
	
	private int chunkX;
	private int chunkZ;
	private IBlockState[] states;
	
	public WorldChunkS2CPacket() {
	}

	public WorldChunkS2CPacket(int chunkX, int chunkZ, IBlockState[] states) {
		if (DebugUtil.PERFORM_CHECKS) {
			if (CHUNK_SIZE * CHUNK_SIZE * CHUNK_HEIGHT != states.length)
				throw new IllegalArgumentException("The states array be an entire chunk!");
		}
		
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.states = Arrays.copyOf(states, states.length);
	}
	
	@Override
	public void encode(PacketEncodeBuffer buffer) {
		buffer.writeInt(chunkX);
		buffer.writeInt(chunkZ);
		
	}

	@Override
	public void decode(PacketDecodeBuffer buffer) {
		chunkX = buffer.readInt();
		chunkZ = buffer.readInt();

		for (int z = 0; z < CHUNK_SIZE; z++) {
			for (int y = 0; y < CHUNK_HEIGHT; y++) {
				for (int x = 0; x < CHUNK_SIZE; x++) {
					
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

	public int getChunkZ() {
		return chunkZ;
	}
	
	public IBlockState[] getStates() {
		return states;
	}
}
