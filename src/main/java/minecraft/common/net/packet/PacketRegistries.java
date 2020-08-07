package minecraft.common.net.packet;

import java.util.function.Supplier;

import minecraft.common.SupplierRegistry;
import minecraft.common.net.NetworkSide;
import minecraft.common.net.packet.universal.HelloWorldUPacket;

public final class PacketRegistries {

	private static final SupplierRegistry<Short, IPacket<?>> serverToClientRegistry;
	private static final SupplierRegistry<Short, IPacket<?>> clientToServerRegistry;

	static {
		serverToClientRegistry = new SupplierRegistry<>();
		clientToServerRegistry = new SupplierRegistry<>();
		
		registerUniversal(0, HelloWorldUPacket.class, HelloWorldUPacket::new);
	}
	
	private PacketRegistries() {
		throw new UnsupportedOperationException();
	}
	
	private static <T extends IPacket<?>> void registerUniversal(int id, Class<T> clazz, Supplier<T> supplier) {
		registerS2C(id, clazz, supplier);
		registerC2S(id, clazz, supplier);
	}

	private static <T extends IPacket<?>> void registerS2C(int id, Class<T> clazz, Supplier<T> supplier) {
		serverToClientRegistry.register((short)id, clazz, supplier);
	}

	private static <T extends IPacket<?>> void registerC2S(int id, Class<T> clazz, Supplier<T> supplier) {
		clientToServerRegistry.register((short)id, clazz, supplier);
	}

	public static final SupplierRegistry<Short, IPacket<?>> getRegistry(NetworkSide receiverSide) {
		switch (receiverSide) {
		case CLIENT:
			return serverToClientRegistry;
		case SERVER:
			return clientToServerRegistry;
		}

		throw new IllegalStateException("Unknown network side: " + receiverSide);
	}
}
