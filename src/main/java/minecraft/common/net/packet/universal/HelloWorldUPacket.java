package minecraft.common.net.packet.universal;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import minecraft.common.net.packet.IPacket;
import minecraft.common.net.packet.IPacketHandler;

public class HelloWorldUPacket implements IPacket<IPacketHandler> {

	private CharSequence msg;
	
	public HelloWorldUPacket() {
	}

	public HelloWorldUPacket(CharSequence msg) {
		this.msg = msg;
	}
	
	@Override
	public void decode(ByteBuf buffer) {
		int length = buffer.readIntLE();
		buffer.readCharSequence(length, CharsetUtil.UTF_8);
	}

	@Override
	public void encode(ByteBuf buffer) {
		buffer.writeIntLE(msg.length());
		buffer.writeCharSequence(msg, CharsetUtil.UTF_8);
	}

	@Override
	public void handle(IPacketHandler handler) {
		System.out.println("Hello world: " + msg);
	}
}
