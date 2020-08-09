package minecraft.common.net.packet;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class PacketDecodeBuffer {

	private final ByteBuf buffer;
	
	private PacketDecodeBuffer(ByteBuf buffer) {
		this.buffer = buffer;
	}
	
	public boolean readBoolean() {
		return buffer.readBoolean();
	}

	public byte readByte() {
		return buffer.readByte();
	}

	public void readBytes(byte[] dst) {
		readBytes(dst, dst.length);
	}

	public void readBytes(byte[] dst, int length) {
		readBytes(dst, 0, length);
	}

	public void readBytes(byte[] dst, int dstOffset, int length) {
		buffer.readBytes(dst, dstOffset, length);
	}
	
	public void skipBytes(int length) {
		buffer.skipBytes(length);
	}

	public short readShort() {
		return buffer.readShortLE();
	}
	
	public int readMedium() {
		return buffer.readMediumLE();
	}
	
	public int readInt() {
		return buffer.readIntLE();
	}

	public long readLong() {
		return buffer.readLongLE();
	}
	
	public float readFloat() {
		return buffer.readFloatLE();
	}

	public double readDouble() {
		return buffer.readDoubleLE();
	}

	public short readUnsignedByte() {
		return buffer.readUnsignedByte();
	}
	
	public int readUnsignedShort() {
		return buffer.readUnsignedShortLE();
	}
	
	public int readUnsignedMedium() {
		return buffer.readUnsignedMediumLE();
	}
	
	public long readUnsignedInt() {
		return buffer.readUnsignedIntLE();
	}

	public CharSequence readString() {
		return readString(CharsetUtil.US_ASCII);
	}

	public CharSequence readString(Charset charset) {
		return buffer.readCharSequence(readInt(), charset);
	}
	
	public boolean getBoolean(int location) {
		return buffer.getBoolean(location);
	}

	public byte getByte(int location) {
		return buffer.getByte(location);
	}
	
	public void getBytes(int location, byte[] dst) {
		getBytes(location, dst, dst.length);
	}

	public void getBytes(int location, byte[] dst, int length) {
		getBytes(location, dst, 0, length);
	}

	public void getBytes(int location, byte[] dst, int dstOffset, int length) {
		buffer.getBytes(location, dst, dstOffset, length);
	}
	
	public short getShort(int location) {
		return buffer.getShortLE(location);
	}

	public int getMedium(int location) {
		return buffer.getMediumLE(location);
	}

	public int getInt(int location) {
		return buffer.getIntLE(location);
	}

	public long getLong(int location) {
		return buffer.getLongLE(location);
	}
	
	public float getFloat(int location) {
		return buffer.getFloatLE(location);
	}

	public double getDouble(int location) {
		return buffer.getDoubleLE(location);
	}
	
	public short getUnsignedByte(int location) {
		return buffer.getUnsignedByte(location);
	}
	
	public int getUnsignedShort(int location) {
		return buffer.getUnsignedShortLE(location);
	}
	
	public int getUnsignedMedium(int location) {
		return buffer.getUnsignedMediumLE(location);
	}
	
	public long getUnsignedInt(int location) {
		return buffer.getUnsignedIntLE(location);
	}
	
	public int getLocation() {
		return buffer.readerIndex();
	}

	public void setLocation(int location) {
		buffer.readerIndex(location);
	}
	
	public void markLocation() {
		buffer.markReaderIndex();
	}

	public void resetLocation() {
		buffer.resetReaderIndex();
	}

	static PacketDecodeBuffer wrap(ByteBuf buffer) {
		return new PacketDecodeBuffer(buffer);
	}
}
