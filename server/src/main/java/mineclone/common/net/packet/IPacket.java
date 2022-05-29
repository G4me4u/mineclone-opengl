package mineclone.common.net.packet;

public interface IPacket {

	public void encode(PacketEncodeBuffer buffer);

	public void decode(PacketDecodeBuffer buffer);
	
}
