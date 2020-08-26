package mineclone.common.net.packet.universal;

import mineclone.common.net.packet.IPacket;
import mineclone.common.net.packet.IPacketHandler;
import mineclone.common.net.packet.PacketDecodeBuffer;
import mineclone.common.net.packet.PacketEncodeBuffer;

public class HelloWorldUPacket implements IPacket<IPacketHandler> {

	private CharSequence msg;
	
	public HelloWorldUPacket() {
	}

	public HelloWorldUPacket(CharSequence msg) {
		this.msg = msg;
	}
	
	@Override
	public void encode(PacketEncodeBuffer buffer) {
		buffer.writeString(msg);
	}

	@Override
	public void decode(PacketDecodeBuffer buffer) {
		msg = buffer.readString();
	}

	@Override
	public void handle(IPacketHandler handler) {
		System.out.println("Hello world: " + msg);
	}
}
