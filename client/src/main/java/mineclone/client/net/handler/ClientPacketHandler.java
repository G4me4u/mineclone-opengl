package mineclone.client.net.handler;

import mineclone.client.MinecloneClient;
import mineclone.common.TaskScheduler;
import mineclone.common.net.INetworkConnection;
import mineclone.common.net.NetworkPhase;
import mineclone.common.net.NetworkSide;
import mineclone.common.net.handler.AbstractPacketHandler;
import mineclone.common.net.handler.IClientPacketHandler;
import mineclone.common.net.packet.PacketRegistries;
import mineclone.common.net.packet.s2c.ChunkS2CPacket;
import mineclone.common.net.packet.universal.StateChangeUPacket;

public class ClientPacketHandler extends AbstractPacketHandler implements IClientPacketHandler {

	private final MinecloneClient client;
	
	public ClientPacketHandler(MinecloneClient client, INetworkConnection connection) {
		super(connection, PacketRegistries.getRegistry(NetworkPhase.GAMEPLAY, NetworkSide.CLIENT));
		
		this.client = client;
	}
	
	@Override
	public void onWorldChunk(ChunkS2CPacket packet) {
		client.getWorld().setChunk(packet.getChunkPos(), packet.getChunk());
	}
	
	@Override
	public void onStateChange(StateChangeUPacket packet) {
		client.getWorld().setBlockState(packet.getPos(), packet.getState());
	}
	
	@Override
	protected TaskScheduler getTaskScheduler() {
		return client.getTaskScheduler();
	}
}
