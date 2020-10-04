package mineclone.common.net.handler;

import mineclone.common.net.packet.s2c.WorldChunkS2CPacket;

public interface IClientPacketHandler extends IPacketHandler {

	public void onWorldChunk(WorldChunkS2CPacket packet);

}
