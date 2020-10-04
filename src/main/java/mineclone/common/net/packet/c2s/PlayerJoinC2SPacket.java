package mineclone.common.net.packet.c2s;

import mineclone.common.net.handler.IServerPacketHandler;
import mineclone.common.net.packet.IPacket;
import mineclone.common.net.packet.PacketDecodeBuffer;
import mineclone.common.net.packet.PacketEncodeBuffer;

public class PlayerJoinC2SPacket implements IPacket<IServerPacketHandler> {

	@Override
	public void encode(PacketEncodeBuffer buffer) {
	}

	@Override
	public void decode(PacketDecodeBuffer buffer) {
	}

	@Override
	public void handle(IServerPacketHandler handler) {
	}
}
