package mineclone.common.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import mineclone.common.net.handler.IPacketHandler;
import mineclone.common.net.packet.IPacket;

public class PacketChannelHandler extends SimpleChannelInboundHandler<IPacket> {

	private final IPacketHandler handler;
	
	public PacketChannelHandler(IPacketHandler handler) {
		this.handler = handler;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IPacket packet) throws Exception {
		handler.onPacket(packet);
	}
}
