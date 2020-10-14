package mineclone.common.net.handler;

import mineclone.common.net.packet.s2c.ChunkS2CPacket;

public interface IClientPacketHandler extends IPacketHandler {

	public void onWorldChunk(ChunkS2CPacket packet);

}
