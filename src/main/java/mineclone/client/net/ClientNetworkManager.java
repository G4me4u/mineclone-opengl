package mineclone.client.net;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Collections;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.SocketUtils;
import mineclone.common.net.NetworkConnection;
import mineclone.common.net.NetworkManager;
import mineclone.common.net.NetworkPhase;
import mineclone.common.net.NetworkSide;
import mineclone.common.net.packet.IPacket;
import mineclone.common.net.packet.PacketCodec;
import mineclone.common.net.packet.universal.HelloWorldUPacket;

public class ClientNetworkManager extends NetworkManager {

	private EventLoopGroup eventGroup;
	
	private NetworkConnection connection;
	
	public ClientNetworkManager() {
		super(NetworkSide.CLIENT);
	}
	
	public void connect(String serverHostname, int serverPort) throws Exception {
		connect(SocketUtils.socketAddress(serverHostname, serverPort));
	}
	
	public void connect(SocketAddress serverAddress) throws Exception {
		Bootstrap b = new Bootstrap();
		
		eventGroup = new NioEventLoopGroup();
		
		b.group(eventGroup);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.SO_KEEPALIVE, true);

		b.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				
				pipeline.addLast(PacketCodec.create(NetworkPhase.HANDSHAKE, NetworkSide.CLIENT), new ChannelInboundHandlerAdapter() {
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						((IPacket<?>)msg).handle(null);
					}
				});
			}
		});
		
		Channel channel = b.connect(serverAddress).sync().channel();
		connection = new NetworkConnection(channel);
		
		channel.writeAndFlush(new HelloWorldUPacket("Hello from client!")).sync();
	}

	@Override
	public Collection<NetworkConnection> getConnections() {
		return Collections.singletonList(connection);
	}

	@Override
	public boolean isActive() {
		return connection.isConnected();
	}
	
	@Override
	public void close() {
		connection.close();
		
		if (eventGroup != null) {
			eventGroup.shutdownGracefully();
			eventGroup = null;
		}
	}
}
