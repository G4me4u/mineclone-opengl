package minecraft.common.net;

import minecraft.common.IResource;
import minecraft.common.net.packet.IPacket;

public interface INetworkConnection extends IResource {

	public void send(IPacket<?> packet);
	
	public boolean isConnected();
	
}
