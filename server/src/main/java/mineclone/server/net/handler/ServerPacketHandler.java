package mineclone.server.net.handler;

import java.util.Iterator;

import mineclone.common.TaskScheduler;
import mineclone.common.net.INetworkConnection;
import mineclone.common.net.NetworkPhase;
import mineclone.common.net.NetworkSide;
import mineclone.common.net.handler.AbstractPacketHandler;
import mineclone.common.net.handler.IServerPacketHandler;
import mineclone.common.net.packet.PacketRegistries;
import mineclone.common.net.packet.c2s.PlayerJoinC2SPacket;
import mineclone.common.net.packet.s2c.ChunkS2CPacket;
import mineclone.common.net.packet.universal.StateChangeUPacket;
import mineclone.common.world.chunk.ChunkEntry;
import mineclone.common.world.chunk.IWorldChunk;
import mineclone.common.world.chunk.IWorldChunkManager;
import mineclone.server.MinecloneServer;

public class ServerPacketHandler extends AbstractPacketHandler implements IServerPacketHandler {

	private final MinecloneServer server;
	
	public ServerPacketHandler(MinecloneServer server, INetworkConnection connection) {
		super(connection, PacketRegistries.getRegistry(NetworkPhase.GAMEPLAY, NetworkSide.SERVER));
		
		this.server = server;
	}

	@Override
	public void onPlayerJoin(PlayerJoinC2SPacket packet) {
		System.out.println("[Server]: Player join packet");
		
		IWorldChunkManager chunkManager = server.getWorld().getChunkManager();
		Iterator<ChunkEntry<IWorldChunk>> itr = chunkManager.chunkIterator();
		while (itr.hasNext()) {
			ChunkEntry<IWorldChunk> entry = itr.next();
			connection.send(new ChunkS2CPacket(entry.getChunkPos(), entry.getChunk()));
		}
	}
	
	@Override
	public void onStateChange(StateChangeUPacket packet) {
		server.getWorld().setBlockState(packet.getPos(), packet.getState(), true);
	}
	
	@Override
	protected TaskScheduler getTaskScheduler() {
		return server.getTaskScheduler();
	}
}
