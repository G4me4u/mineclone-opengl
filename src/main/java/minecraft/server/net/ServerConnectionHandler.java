package minecraft.server.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import minecraft.common.net.NetworkConnection;
import minecraft.common.net.packet.IPacket;
import minecraft.common.net.packet.IServerPacketHandler;

public class ServerConnectionHandler extends SimpleChannelInboundHandler<IPacket<IServerPacketHandler>> {

	private final ServerNetworkManager networkManager;
	
	private NetworkConnection connection;
	private ServerPacketHandler handler;
	
	public ServerConnectionHandler(ServerNetworkManager networkManager) {
		this.networkManager = networkManager;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	
		connection = new NetworkConnection(ctx.channel());
		
		networkManager.onConnectionAdded(connection);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);

		connection = null;
		
		networkManager.onConnectionRemoved(connection);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IPacket<IServerPacketHandler> packet) throws Exception {
		packet.handle(handler);
	}
}
