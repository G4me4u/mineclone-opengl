package mineclone.common.net.packet;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import mineclone.common.SupplierRegistry;
import mineclone.common.net.NetworkPhase;
import mineclone.common.net.NetworkSide;
import mineclone.common.net.packet.universal.HelloWorldUPacket;

public final class PacketRegistries {

	private static final Map<NetworkPhase, RegistryBank> registryBanks = new EnumMap<>(NetworkPhase.class);

	static {
		RegistryBank bank;
		
		bank = getRegistryBank(NetworkPhase.HANDSHAKE);
		bank.registerUniversal(0, HelloWorldUPacket.class, HelloWorldUPacket::new);
	}
	
	private PacketRegistries() {
		throw new UnsupportedOperationException();
	}
	
	static SupplierRegistry<Short, IPacket<?>> getRegistry(NetworkPhase phase, NetworkSide receiverSide) {
		return getRegistryBank(phase).getRegistry(receiverSide);
	}
	
	private static RegistryBank getRegistryBank(NetworkPhase phase) {
		RegistryBank bank = registryBanks.get(phase);
		
		if (bank == null) {
			bank = new RegistryBank();
			registryBanks.put(phase, bank);
		}
		
		return bank;
	}
	
	private static class RegistryBank {
		
		private final SupplierRegistry<Short, IPacket<?>> serverToClientRegistry;
		private final SupplierRegistry<Short, IPacket<?>> clientToServerRegistry;
		
		public RegistryBank() {
			serverToClientRegistry = new SupplierRegistry<>();
			clientToServerRegistry = new SupplierRegistry<>();
		}
		
		private <T extends IPacket<?>> void registerUniversal(int packetId, Class<T> clazz, Supplier<T> supplier) {
			registerS2C(packetId, clazz, supplier);
			registerC2S(packetId, clazz, supplier);
		}
		
		private <T extends IPacket<?>> void registerS2C(int packetId, Class<T> clazz, Supplier<T> supplier) {
			serverToClientRegistry.register(checkAndCastPacketId(packetId), clazz, supplier);
		}

		private <T extends IPacket<?>> void registerC2S(int packetId, Class<T> clazz, Supplier<T> supplier) {
			clientToServerRegistry.register(checkAndCastPacketId(packetId), clazz, supplier);
		}
		
		private static short checkAndCastPacketId(int packetId) {
			if ((packetId & (~0xFFFF)) != 0)
				throw new IllegalArgumentException("Packet id is not an unsigned short.");
			
			return (short)packetId;
		}
		
		private SupplierRegistry<Short, IPacket<?>> getRegistry(NetworkSide receiverSide) {
			switch (receiverSide) {
			case CLIENT:
				return serverToClientRegistry;
			case SERVER:
				return clientToServerRegistry;
			}
			
			throw new IllegalStateException("Unknown network side: " + receiverSide);
		}
	}
}
