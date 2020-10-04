package mineclone.common.net;

import io.netty.channel.Channel;
import mineclone.common.IResource;

public abstract class NetworkSocket implements IResource {

	public abstract Channel getSocket();

	public boolean isActive() {
		Channel socket = getSocket();
		
		return (socket != null && socket.isActive());
	}
}
