package minecraft.common.net.packet.c2s;

import minecraft.common.net.handler.IServerPacketHandler;
import minecraft.common.net.packet.IPacket;
import minecraft.common.net.packet.PacketDecodeBuffer;
import minecraft.common.net.packet.PacketEncodeBuffer;

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
