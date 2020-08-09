package minecraft.common.net.packet;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class PacketEncodeBuffer {

	private final ByteBuf buffer;
	
	private PacketEncodeBuffer(ByteBuf buffer) {
		this.buffer = buffer;
	}
	
	public void writeBoolean(boolean value) {
		buffer.writeBoolean(value);
	}

	public void writeByte(byte value) {
		buffer.writeByte(value);
	}
	
	public void writeBytes(byte[] src) {
		writeBytes(src, src.length);
	}

	public void writeBytes(byte[] src, int length) {
		writeBytes(src, 0, length);
	}

	public void writeBytes(byte[] src, int srcOffset, int length) {
		buffer.writeBytes(src, srcOffset, length);
	}
	
	public void writeZero(int length) {
		buffer.writeZero(length);
	}

	public void writeShort(short value) {
		buffer.writeShortLE(value);
	}
	
	public void writeMedium(int value) {
		buffer.writeMediumLE(value);
	}
	
	public void writeInt(int value) {
		buffer.writeIntLE(value);
	}

	public void writeLong(long value) {
		buffer.writeLongLE(value);
	}
	
	public void writeFloat(float value) {
		buffer.writeFloatLE(value);
	}

	public void writeDouble(double value) {
		buffer.writeDoubleLE(value);
	}

	public void writeUnsignedByte(short value) {
		buffer.writeByte(value);
	}
	
	public void writeUnsignedShort(int value) {
		buffer.writeShortLE(value);
	}
	
	public void writeUnsignedMedium(int value) {
		buffer.writeMediumLE(value);
	}
	
	public void writeUnsignedInt(long value) {
		buffer.writeIntLE((int)value);
	}

	public void writeString(CharSequence str) {
		writeString(str, CharsetUtil.US_ASCII);
	}

	public void writeString(CharSequence str, Charset charset) {
		writeInt(str.length());
		
		buffer.writeCharSequence(str, charset);
	}
	
	public void setBoolean(int location, boolean value) {
		buffer.setBoolean(location, value);
	}

	public void setByte(int location, byte value) {
		buffer.setByte(location, value);
	}
	
	public void setBytes(int location, byte[] src) {
		setBytes(location, src, src.length);
	}

	public void setBytes(int location, byte[] src, int length) {
		setBytes(location, src, 0, length);
	}

	public void setBytes(int location, byte[] src, int srcOffset, int length) {
		buffer.setBytes(location, src, srcOffset, length);
	}
	
	public void setShort(int location, short value) {
		buffer.setShortLE(location, value);
	}

	public void setMedium(int location, int value) {
		buffer.setMediumLE(location, value);
	}

	public void setInt(int location, int value) {
		buffer.setIntLE(location, value);
	}

	public void setLong(int location, long value) {
		buffer.setLongLE(location, value);
	}
	
	public void setFloat(int location, float value) {
		buffer.setFloatLE(location, value);
	}

	public void setDouble(int location, double value) {
		buffer.setDoubleLE(location, value);
	}
	
	public void setUnsignedByte(int location, short value) {
		buffer.setByte(location, value);
	}
	
	public void setUnsignedShort(int location, int value) {
		buffer.setShortLE(location, value);
	}
	
	public void setUnsignedMedium(int location, int value) {
		buffer.setMediumLE(location, value);
	}
	
	public void setUnsignedInt(int location, long value) {
		buffer.setIntLE(location, (int)value);
	}
	
	public int getLocation() {
		return buffer.writerIndex();
	}

	public void setLocation(int location) {
		buffer.writerIndex(location);
	}
	
	public void markLocation() {
		buffer.markWriterIndex();
	}

	public void resetLocation() {
		buffer.resetWriterIndex();
	}

	static PacketEncodeBuffer wrap(ByteBuf buffer) {
		return new PacketEncodeBuffer(buffer);
	}
}
