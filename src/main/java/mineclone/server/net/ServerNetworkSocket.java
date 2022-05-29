package mineclone.server.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.SocketUtils;
import mineclone.common.net.INetworkConnection;
import mineclone.common.net.NetworkManager;
import mineclone.common.net.NetworkPhase;
import mineclone.common.net.NetworkSide;
import mineclone.common.net.NetworkSocket;
import mineclone.common.net.PacketChannelHandler;
import mineclone.common.net.SocketNetworkConnection;
import mineclone.common.net.packet.PacketCodec;
import mineclone.server.MinecloneServer;
import mineclone.server.net.handler.ServerPacketHandler;

public class ServerNetworkSocket extends NetworkSocket {

	private final MinecloneServer server;
	private final NetworkManager manager;
	
	private EventLoopGroup parentGroup;
	private EventLoopGroup childGroup;
	
	private Channel socket;
	
	public ServerNetworkSocket(MinecloneServer server, NetworkManager manager) {
		this.server = server;
		this.manager = manager;
	}
	
	public void bind(int port) throws Exception {
		bind(new InetSocketAddress(port));
	}

	public void bind(String hostname, int port) throws Exception {
		bind(SocketUtils.socketAddress(hostname, port));
	}

	public void bind(SocketAddress address) throws Exception {
		if (socket != null)
			throw new IllegalStateException("Already bound");
		
		ServerBootstrap b = new ServerBootstrap();

		parentGroup = new NioEventLoopGroup();
		childGroup = new NioEventLoopGroup();
		b.group(parentGroup, childGroup);
		b.channel(NioServerSocketChannel.class);
		
		b.option(ChannelOption.SO_BACKLOG, 128);
		b.childOption(ChannelOption.SO_KEEPALIVE, true);
		b.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel channel) throws Exception {
				INetworkConnection connection = new SocketNetworkConnection(channel);
				
				ChannelPipeline pipeline = channel.pipeline();
				
				pipeline.addLast(PacketCodec.create(NetworkPhase.GAMEPLAY, NetworkSide.SERVER));
				pipeline.addLast(new PacketChannelHandler(new ServerPacketHandler(server, connection)));

				manager.addConnection(connection);
			}
		});
		
		socket = b.bind(address).sync().channel();
	}

	@Override
	public Channel getSocket() {
		return socket;
	}
	
	@Override
	public void close() {
		if (socket != null) {
			socket.close().awaitUninterruptibly();
			socket = null;
		}
		
		if (parentGroup != null) {
			parentGroup.shutdownGracefully();
			parentGroup = null;
		}
		
		if (childGroup != null) {
			childGroup.shutdownGracefully();
			childGroup = null;
		}
	}
}
