package mineclone.common.net.packet;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import mineclone.common.net.NetworkPhase;
import mineclone.common.net.NetworkSide;
import mineclone.common.util.DebugUtil;

public class PacketCodec extends ByteToMessageCodec<IPacket> {

	private static final int LENGTH_BYTE_SIZE      = 2;
	private static final int IDENTIFIER_BYTE_SIZE  = 2;
	private static final int HEADER_BYTE_SIZE      = LENGTH_BYTE_SIZE + IDENTIFIER_BYTE_SIZE;
	
	private static final int MAXIMUM_PACKET_LENGTH = 0xFFFF; // 2^16 - 1
	
	private final PacketRegistry<?> inboundRegistry;
	private final PacketRegistry<?> outboundRegistry;
	
	private PacketCodec(PacketRegistry<?> inboundRegistry, PacketRegistry<?> outboundRegistry) {
		this.inboundRegistry = inboundRegistry;
		this.outboundRegistry = outboundRegistry;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, IPacket packet, ByteBuf outBuffer) throws Exception {
		if (DebugUtil.PERFORM_CHECKS) {
			if (!outboundRegistry.containsPacket(packet))
				throw new EncoderException("Attempting to encode packet which is not in registry: " + packet);
		}
		
		int lengthIndex = outBuffer.writerIndex();
		outBuffer.writeZero(LENGTH_BYTE_SIZE);

		short packetId = outboundRegistry.getIdentifier(packet);
		outBuffer.writeShortLE(packetId);
		
		packet.encode(PacketEncodeBuffer.wrap(outBuffer));
		
		int packetLength = outBuffer.writerIndex() - (lengthIndex + HEADER_BYTE_SIZE);
		
		if (packetLength > MAXIMUM_PACKET_LENGTH)
			throw new EncoderException("Packet length exceeds maximum (" + MAXIMUM_PACKET_LENGTH + "): " + packetLength);
		
		outBuffer.setShortLE(lengthIndex, (short)packetLength);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		if (buffer.isReadable(HEADER_BYTE_SIZE)) {
			int packetLength = buffer.getUnsignedShortLE(buffer.readerIndex());
			if (packetLength > MAXIMUM_PACKET_LENGTH)
				throw new DecoderException("Packet length exceeds maximum (" + MAXIMUM_PACKET_LENGTH + "): " + packetLength);
			
			if (buffer.isReadable(packetLength + HEADER_BYTE_SIZE)) {
				buffer.skipBytes(LENGTH_BYTE_SIZE);
				
				short packetId = buffer.readShortLE();
				
				IPacket packet = inboundRegistry.createPacket(packetId);
				if (packet == null)
					throw new DecoderException("Attempting to decode unknown packet: " + packetId);

				ByteBuf bufferSlice = buffer.readSlice(packetLength);
				packet.decode(PacketDecodeBuffer.wrap(bufferSlice));
				
				out.add(packet);
			}
		}
	}
	
	public static PacketCodec create(NetworkPhase phase, NetworkSide side) {
		return new PacketCodec(PacketRegistries.getRegistry(phase, side),
		                       PacketRegistries.getRegistry(phase, side.getOpposite()));
	}
}
