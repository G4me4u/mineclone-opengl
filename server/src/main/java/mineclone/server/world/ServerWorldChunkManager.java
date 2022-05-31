package mineclone.server.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import mineclone.common.net.packet.universal.StateChangeUPacket;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.ChunkEntry;
import mineclone.common.world.chunk.ChunkPosition;
import mineclone.common.world.chunk.EmptyWorldChunk;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunk;
import mineclone.common.world.chunk.IWorldChunkManager;
import mineclone.common.world.chunk.StaticChunkStorage;
import mineclone.common.world.chunk.StaticHeightMap;
import mineclone.common.world.chunk.WorldChunk;
import mineclone.server.MinecloneServer;

public class ServerWorldChunkManager implements IWorldChunkManager {

	private final MinecloneServer server;
	
	private final StaticChunkStorage<IWorldChunk> storage;
	private final StaticHeightMap heightMap;
	
	private final Set<IBlockPosition> dirtyPositions;
	
	public ServerWorldChunkManager(MinecloneServer server) {
		this.server = server;
		
		// TODO: make this dynamic :-)
		storage = new StaticChunkStorage<IWorldChunk>(EmptyWorldChunk.INSTANCE);
		heightMap = new StaticHeightMap();
		
		dirtyPositions = new HashSet<>();
	}
	
	public IWorldChunk getChunk(IChunkPosition chunkPos, boolean initAbsent) {
		IWorldChunk chunk = storage.getChunk(chunkPos);
		if (initAbsent && chunk == EmptyWorldChunk.INSTANCE) {
			chunk = new WorldChunk();
			// Note: no need to update height map, since
			//       the entire chunk is air.
			storage.setChunk(chunkPos, chunk);
		}
		return chunk;
	}

	@Override
	public IWorldChunk getChunk(IChunkPosition chunkPos) {
		return getChunk(chunkPos, false);
	}

	@Override
	public boolean setChunk(IChunkPosition chunkPos, IWorldChunk chunk) {
		if (storage.setChunk(chunkPos, chunk)) {
			heightMap.updateHeightMapChunk(chunkPos, this);
			return true;
		}
		
		return false;
	}
	
	@Override
	public IBlockState getBlockState(IBlockPosition pos) {
		IWorldChunk chunk = getChunk(new ChunkPosition(pos), false);

		int rx = pos.getX() & IWorldChunk.CHUNK_MASK;
		int ry = pos.getY() & IWorldChunk.CHUNK_MASK;
		int rz = pos.getZ() & IWorldChunk.CHUNK_MASK;
		
		return chunk.getBlockState(rx, ry, rz);
	}

	@Override
	public boolean setBlockState(IBlockPosition pos, IBlockState state) {
		IWorldChunk chunk = getChunk(new ChunkPosition(pos), !state.isAir());

		int rx = pos.getX() & IWorldChunk.CHUNK_MASK;
		int ry = pos.getY() & IWorldChunk.CHUNK_MASK;
		int rz = pos.getZ() & IWorldChunk.CHUNK_MASK;
		
		if (chunk.setBlockState(rx, ry, rz, state)) {
			// TODO: make the dirty positions more efficient.
			dirtyPositions.add(pos);
			heightMap.updateHeightMap(pos, this);
			return true;
		}
		
		return false;
	}
	
	public void broadcastDirtyStates() {
		for (IBlockPosition pos : dirtyPositions)
			server.sendToAll(new StateChangeUPacket(pos, getBlockState(pos)));
		dirtyPositions.clear();
	}

	@Override
	public int getHighestPoint(int x, int z) {
		return heightMap.getHighestPoint(x, z);
	}

	@Override
	public boolean containsChunk(IChunkPosition chunkPos) {
		return storage.contains(chunkPos);
	}

	@Override
	public Iterator<ChunkEntry<IWorldChunk>> chunkIterator() {
		return storage.chunkIterator();
	}
}
