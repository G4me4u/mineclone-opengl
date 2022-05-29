package mineclone.common.net.handler;

import mineclone.common.net.packet.IPacket;

public interface IPacketHandler {

	public void onPacket(IPacket packet);

}
