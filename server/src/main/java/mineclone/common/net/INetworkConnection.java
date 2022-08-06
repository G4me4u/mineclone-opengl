package mineclone.common.net;

import mineclone.common.IResource;
import mineclone.common.net.packet.IPacket;

public interface INetworkConnection extends IResource {

	void send(IPacket packet);
	
	boolean isOpen();
	
}
