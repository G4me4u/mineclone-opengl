package minecraft.server.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.SocketUtils;
import minecraft.common.net.NetworkManager;
import minecraft.common.net.NetworkSide;
import minecraft.common.net.packet.PacketCodec;

public class ServerNetworkManager extends NetworkManager {

	private EventLoopGroup parentGroup;
	private EventLoopGroup childGroup;

	private Channel channel;
	
	public ServerNetworkManager() {
		super(NetworkSide.SERVER);
	}

	public void bind(int port) throws Exception {
		bind(new InetSocketAddress(port));
	}

	public void bind(String hostname, int port) throws Exception {
		bind(SocketUtils.socketAddress(hostname, port));
	}

	public void bind(SocketAddress address) throws Exception {
		if (channel != null)
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
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(PacketCodec.create(NetworkSide.SERVER));
			};
		});
		
		channel = b.bind(address).sync().channel();
	}

	@Override
	public void close() {
		if (channel != null) {
			channel.close().awaitUninterruptibly();
			channel = null;
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
