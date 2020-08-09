package minecraft.common.net.packet;

public interface IPacket<T extends IPacketHandler> {

	public void encode(PacketEncodeBuffer buffer);

	public void decode(PacketDecodeBuffer buffer);
	
	public void handle(T handler);
	
}
