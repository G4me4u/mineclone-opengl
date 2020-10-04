package minecraft.common.net.packet.s2c;

import java.util.Arrays;

import minecraft.common.net.handler.IClientPacketHandler;
import minecraft.common.net.packet.IPacket;
import minecraft.common.net.packet.PacketDecodeBuffer;
import minecraft.common.net.packet.PacketEncodeBuffer;
import minecraft.common.util.DebugUtil;
import minecraft.common.world.World;
import minecraft.common.world.WorldChunk;
import minecraft.common.world.block.state.IBlockState;

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
