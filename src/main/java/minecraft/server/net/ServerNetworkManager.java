package minecraft.server.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.SocketUtils;
import minecraft.common.net.NetworkConnection;
import minecraft.common.net.NetworkManager;
import minecraft.common.net.NetworkPhase;
import minecraft.common.net.NetworkSide;
import minecraft.common.net.packet.IPacket;
import minecraft.common.net.packet.PacketCodec;
import minecraft.common.net.packet.universal.HelloWorldUPacket;

public class ServerNetworkManager extends NetworkManager {

	private EventLoopGroup parentGroup;
	private EventLoopGroup childGroup;

	private Channel serverChannel;
	
	private final List<NetworkConnection> connections;
	
	public ServerNetworkManager() {
		super(NetworkSide.SERVER);
		
		connections = new ArrayList<>();
	}

	public void bind(int port) throws Exception {
		bind(new InetSocketAddress(port));
	}

	public void bind(String hostname, int port) throws Exception {
		bind(SocketUtils.socketAddress(hostname, port));
	}

	public void bind(SocketAddress address) throws Exception {
		if (serverChannel != null)
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
				ChannelPipeline pipeline = channel.pipeline();
				
				pipeline.addLast(PacketCodec.create(NetworkPhase.HANDSHAKE, NetworkSide.SERVER));
				pipeline.addLast(new SimpleChannelInboundHandler<IPacket<?>>() {
					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						ctx.channel().writeAndFlush(new HelloWorldUPacket("Hello from server!"));
					}
					
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, IPacket<?> packet) throws Exception {
						((IPacket<?>)packet).handle(null);
					}
				});
			};
		});
		
		serverChannel = b.bind(address).sync().channel();
	}
	
	public ServerPacketHandler onConnectionAdded(NetworkConnection connection) {
		connections.add(connection);
		return null;
	}

	public ServerPacketHandler onConnectionRemoved(NetworkConnection connection) {
		connections.remove(connection);
		return null;
	}

	@Override
	public Collection<NetworkConnection> getConnections() {
		return Collections.unmodifiableCollection(connections);
	}
	
	@Override
	public boolean isActive() {
		return (serverChannel != null && serverChannel.isActive());
	}
	
	@Override
	public void close() {
		if (serverChannel != null) {
			serverChannel.close().awaitUninterruptibly();
			serverChannel = null;
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
