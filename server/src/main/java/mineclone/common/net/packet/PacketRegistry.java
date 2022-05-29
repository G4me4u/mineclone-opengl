package mineclone.common.net.packet;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import mineclone.common.ReferenceRegsitry;
import mineclone.common.SupplierRegistry;
import mineclone.common.net.handler.IPacketHandler;

public class PacketRegistry<H extends IPacketHandler> {
	
	private final SupplierRegistry<Short, IPacket> packetRegistry;
	private final ReferenceRegsitry<Class<? extends IPacket>, BiConsumer<H, ? extends IPacket>> handlerRegistry;
	private final Set<Class<? extends IPacket>> synchronousRegistry;
	
	PacketRegistry() {
		packetRegistry = new SupplierRegistry<>();
		handlerRegistry = new ReferenceRegsitry<>();
		synchronousRegistry = Collections.newSetFromMap(new IdentityHashMap<>());
	}
	
	<P extends IPacket> void register(int packetId, Class<P> clazz, Supplier<P> supplier, BiConsumer<H, P> handler, boolean synchronous) {
		packetRegistry.register(checkAndCastPacketId(packetId), clazz, supplier);
		handlerRegistry.register(clazz, handler);
		if (synchronous)
			synchronousRegistry.add(clazz);
	}
	
	private static short checkAndCastPacketId(int packetId) {
		if ((packetId & (~0xFFFF)) != 0)
			throw new IllegalArgumentException("Packet id is not an unsigned short.");
		
		return (short)packetId;
	}
	
	public boolean containsPacket(IPacket packet) {
		return packetRegistry.containsElement(packet);
	}

	public short getIdentifier(IPacket packet) {
		return packetRegistry.getIdentifier(packet);
	}

	public IPacket createPacket(short id) {
		return packetRegistry.createNewElement(id);
	}
	
	public <P extends IPacket> BiConsumer<? extends IPacketHandler, P> getHandler(P packet) {
		Class<? extends IPacket> clazz = packet.getClass();
		@SuppressWarnings("unchecked")
		BiConsumer<? extends IPacketHandler, P> handler = (BiConsumer<? extends IPacketHandler, P>)handlerRegistry.getElement(clazz);
		return handler;
	}

	public boolean isSynchronous(IPacket packet) {
		return synchronousRegistry.contains(packet.getClass());
	}
}
