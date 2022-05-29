package mineclone.client.net;

import java.net.SocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.SocketUtils;
import mineclone.client.MinecloneClient;
import mineclone.client.net.handler.ClientPacketHandler;
import mineclone.common.net.INetworkConnection;
import mineclone.common.net.NetworkManager;
import mineclone.common.net.NetworkPhase;
import mineclone.common.net.NetworkSide;
import mineclone.common.net.NetworkSocket;
import mineclone.common.net.PacketChannelHandler;
import mineclone.common.net.SocketNetworkConnection;
import mineclone.common.net.packet.PacketCodec;

public class ClientNetworkSocket extends NetworkSocket {

	private final MinecloneClient client;
	private final NetworkManager manager;

	private INetworkConnection connection;
	
	private EventLoopGroup eventGroup;
	private Channel socket;

	public ClientNetworkSocket(MinecloneClient client, NetworkManager manager) {
		this.client = client;
		this.manager = manager;
		
		connection = null;
	}
	
	public void connect(String serverHostname, int serverPort) throws Exception {
		connect(SocketUtils.socketAddress(serverHostname, serverPort));
	}
	
	private void connect(SocketAddress serverAddress) throws Exception {
		Bootstrap b = new Bootstrap();
		
		eventGroup = new NioEventLoopGroup();
		
		b.group(eventGroup);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel channel) throws Exception {
				connection = new SocketNetworkConnection(channel);
				
				ChannelPipeline pipeline = channel.pipeline();
				
				pipeline.addLast(PacketCodec.create(NetworkPhase.GAMEPLAY, NetworkSide.CLIENT));
				pipeline.addLast(new PacketChannelHandler(new ClientPacketHandler(client, connection)));
			}
		});
		
		socket = b.connect(serverAddress).sync().channel();
		manager.addConnection(connection);
	}

	@Override
	public Channel getSocket() {
		return socket;
	}
	
	@Override
	public void close() {
		INetworkConnection serverConnection = this.connection;
		if (serverConnection != null) {
			manager.removeConnection(serverConnection);
			this.connection = null;
		}

		Channel socket = this.socket;
		if (socket != null) {
			socket.close().awaitUninterruptibly();
			this.socket = null;
		}
		
		EventLoopGroup eventGroup = this.eventGroup;
		if (eventGroup != null) {
			eventGroup.shutdownGracefully();
			this.eventGroup = null;
		}
	}
}
