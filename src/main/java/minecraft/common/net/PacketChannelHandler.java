package minecraft.common.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import minecraft.common.net.handler.IPacketHandler;
import minecraft.common.net.packet.IPacket;

public class PacketChannelHandler extends SimpleChannelInboundHandler<IPacket<?>> {

	private final IPacketHandler handler;
	
	public PacketChannelHandler(IPacketHandler handler) {
		this.handler = handler;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IPacket<?> packet) throws Exception {
		handlePacket(packet);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends IPacketHandler> void handlePacket(IPacket<T> packet) {
		packet.handle((T)handler);
	}
}
