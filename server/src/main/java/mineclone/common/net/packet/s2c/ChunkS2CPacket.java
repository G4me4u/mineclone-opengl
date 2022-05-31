package mineclone.common.net.packet.s2c;

import mineclone.common.net.packet.IPacket;
import mineclone.common.net.packet.PacketDecodeBuffer;
import mineclone.common.net.packet.PacketEncodeBuffer;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.ChunkPosition;
import mineclone.common.world.chunk.EmptyWorldChunk;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunk;
import mineclone.common.world.chunk.WorldChunk;

public class ChunkS2CPacket implements IPacket {

	private IChunkPosition chunkPos;
	private IWorldChunk chunk;
	
	public ChunkS2CPacket() {
	}

	public ChunkS2CPacket(IChunkPosition chunkPos, IWorldChunk chunk) {
		this.chunkPos = new ChunkPosition(chunkPos);
		this.chunk = chunk.isEmpty() ? EmptyWorldChunk.INSTANCE : new WorldChunk(chunk);
	}
	
	@Override
	public void encode(PacketEncodeBuffer buffer) {
		buffer.writeInt(chunkPos.getChunkX());
		buffer.writeInt(chunkPos.getChunkY());
		buffer.writeInt(chunkPos.getChunkZ());

		buffer.writeBoolean(chunk.isEmpty());
		if (!chunk.isEmpty()) {
			for (int rz = 0; rz < IWorldChunk.CHUNK_SIZE; rz++) {
				for (int ry = 0; ry < IWorldChunk.CHUNK_SIZE; ry++) {
					for (int rx = 0; rx < IWorldChunk.CHUNK_SIZE; rx++) {
						IBlockState state = chunk.getBlockState(rx, ry, rz);
						buffer.writeInt(Blocks.getIdentifier(state));
					}
				}
			}
		}
	}

	@Override
	public void decode(PacketDecodeBuffer buffer) {
		int chunkX = buffer.readInt();
		int chunkY = buffer.readInt();
		int chunkZ = buffer.readInt();
		chunkPos = new ChunkPosition(chunkX, chunkY, chunkZ);

		if (buffer.readBoolean()) {
			chunk = EmptyWorldChunk.INSTANCE;
		} else {
			chunk = new WorldChunk();
			for (int rz = 0; rz < IWorldChunk.CHUNK_SIZE; rz++) {
				for (int ry = 0; ry < IWorldChunk.CHUNK_SIZE; ry++) {
					for (int rx = 0; rx < IWorldChunk.CHUNK_SIZE; rx++) {
						IBlockState state = Blocks.getState(buffer.readInt());
						if (state != null)
							chunk.setBlockState(rx, ry, rz, state);
					}
				}
			}
		}
	}
	
	public IChunkPosition getChunkPos() {
		return chunkPos;
	}
	
	public IWorldChunk getChunk() {
		return chunk;
	}
}
