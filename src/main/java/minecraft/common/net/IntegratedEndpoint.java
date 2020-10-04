package minecraft.common.net;

import minecraft.common.net.handler.IPacketHandler;
import minecraft.common.net.packet.IPacket;

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
