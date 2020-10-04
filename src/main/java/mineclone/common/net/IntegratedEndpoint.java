package mineclone.common.net;

import mineclone.common.net.handler.IPacketHandler;
import mineclone.common.net.packet.IPacket;

public class IntegratedEndpoint {

	private final IPacketHandler handler;
	
	public IntegratedEndpoint(IPacketHandler handler) {
		this.handler = handler;
	}

	@SuppressWarnings("unchecked")
	public <T extends IPacketHandler> void handlePacket(IPacket<T> packet) {
		packet.handle((T)handler);
	}
}
