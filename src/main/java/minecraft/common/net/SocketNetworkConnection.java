package minecraft.common.net;

import io.netty.channel.socket.SocketChannel;
import minecraft.common.net.packet.IPacket;

public class SocketNetworkConnection implements INetworkConnection {

	private final SocketChannel channel;
	
	public SocketNetworkConnection(SocketChannel channel) {
		this.channel = channel;
	}
	
	public SocketChannel getChannel() {
		return channel;
	}

	@Override
	public void send(IPacket<?> packet) {
		channel.write(packet);
	}
	
	@Override
	public boolean isConnected() {
		return channel.isActive();
	}
	
	@Override
	public void close() {
		channel.close().awaitUninterruptibly();
	}
}
