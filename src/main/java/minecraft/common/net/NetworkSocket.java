package minecraft.common.net;

import io.netty.channel.Channel;
import minecraft.common.IResource;

public abstract class NetworkSocket implements IResource {

	public abstract Channel getSocket();

	public boolean isActive() {
		Channel socket = getSocket();
		
		return (socket != null && socket.isActive());
	}
}
