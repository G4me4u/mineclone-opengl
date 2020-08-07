package minecraft.common.net.packet;

import io.netty.buffer.ByteBuf;

public interface IPacket<T extends IPacketHandler> {

	public void decode(ByteBuf buffer);

	public void encode(ByteBuf buffer);
	
	public void handle(T handler);
	
}
