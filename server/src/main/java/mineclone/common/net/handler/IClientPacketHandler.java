package mineclone.common.net.handler;

import mineclone.common.net.packet.s2c.ChunkS2CPacket;
import mineclone.common.net.packet.universal.StateChangeUPacket;

public interface IClientPacketHandler extends IPacketHandler {

	public void onWorldChunk(ChunkS2CPacket packet);

	public void onStateChange(StateChangeUPacket packet);
	
}
