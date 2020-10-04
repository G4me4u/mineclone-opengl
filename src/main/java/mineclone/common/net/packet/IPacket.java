package mineclone.common.net.packet;

import mineclone.common.net.handler.IPacketHandler;

public interface IPacket<T extends IPacketHandler> {

	public void encode(PacketEncodeBuffer buffer);

	public void decode(PacketDecodeBuffer buffer);
	
	public void handle(T handler);
	
}
