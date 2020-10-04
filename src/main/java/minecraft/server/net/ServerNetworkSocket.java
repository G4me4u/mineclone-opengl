package minecraft.server.net;

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
import minecraft.common.net.INetworkConnection;
import minecraft.common.net.NetworkPhase;
import minecraft.common.net.NetworkSide;
import minecraft.common.net.NetworkSocket;
import minecraft.common.net.PacketChannelHandler;
import minecraft.common.net.SocketNetworkConnection;
import minecraft.common.net.packet.PacketCodec;
import minecraft.server.net.handler.ServerPacketHandler;

public class ServerNetworkSocket extends NetworkSocket {

	private EventLoopGroup parentGroup;
	private EventLoopGroup childGroup;
	
	private Channel socket;
	
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
				pipeline.addLast(new PacketChannelHandler(new ServerPacketHandler(connection)));

				// TODO: add the connection to something? :D
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
