package mineclone.common.net.packet.s2c;

import mineclone.common.net.packet.IPacket;
import mineclone.common.net.packet.PacketDecodeBuffer;
import mineclone.common.net.packet.PacketEncodeBuffer;
import mineclone.common.world.chunk.ChunkPosition;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunk;
import mineclone.common.world.chunk.WorldChunk;

public class ChunkS2CPacket implements IPacket {

	private IChunkPosition chunkPos;
	private WorldChunk chunk;
	
	public ChunkS2CPacket() {
	}

	public ChunkS2CPacket(IChunkPosition chunkPos, IWorldChunk chunk) {
		this.chunkPos = new ChunkPosition(chunkPos);
		this.chunk = (chunk == null) ? null : new WorldChunk(chunk);
	}
	
	@Override
	public void encode(PacketEncodeBuffer buffer) {
		buffer.writeInt(chunkPos.getChunkX());
		buffer.writeInt(chunkPos.getChunkY());
		buffer.writeInt(chunkPos.getChunkZ());
		
		if (chunk != null) {
			buffer.writeBoolean(true);
			WorldChunk.write(buffer, chunk);
		} else {
			buffer.writeBoolean(false);
		}
	}

	@Override
	public void decode(PacketDecodeBuffer buffer) {
		int chunkX = buffer.readInt();
		int chunkY = buffer.readInt();
		int chunkZ = buffer.readInt();
		chunkPos = new ChunkPosition(chunkX, chunkY, chunkZ);

		if (buffer.readBoolean()) {
			chunk = WorldChunk.read(buffer);
		} else {
			chunk = null;
		}
	}
	
	public IChunkPosition getChunkPos() {
		return chunkPos;
	}
	
	public IWorldChunk getChunk() {
		return chunk;
	}
}
