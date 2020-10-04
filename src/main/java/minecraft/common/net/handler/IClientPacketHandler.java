package minecraft.common.net.handler;

import minecraft.common.net.packet.s2c.WorldChunkS2CPacket;

public interface IClientPacketHandler extends IPacketHandler {

	public void onWorldChunk(WorldChunkS2CPacket packet);

}
