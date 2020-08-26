package mineclone.common.net;

import io.netty.channel.Channel;
import mineclone.common.IResource;
import mineclone.common.net.packet.IPacket;

public class NetworkConnection implements IResource {

	private final Channel channel;
	
	public NetworkConnection(Channel channel) {
		this.channel = channel;
	}
	
	public void sendPacket(IPacket<?> packet) {
		channel.write(packet);
	}
	
	public boolean isConnected() {
		return channel.isActive();
	}
	
	@Override
	public void close() {
		channel.close().awaitUninterruptibly();
	}
}
