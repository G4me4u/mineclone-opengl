package minecraft.common.net.packet;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import minecraft.common.SupplierRegistry;
import minecraft.common.net.NetworkPhase;
import minecraft.common.net.NetworkSide;
import minecraft.common.net.handler.IClientPacketHandler;
import minecraft.common.net.handler.IServerPacketHandler;
import minecraft.common.net.packet.c2s.PlayerJoinC2SPacket;
import minecraft.common.net.packet.s2c.WorldChunkS2CPacket;

public final class PacketRegistries {

	private static final Map<NetworkPhase, RegistryBank> registryBanks = new EnumMap<>(NetworkPhase.class);

	static {
		RegistryBank bank;
		
		bank = getRegistryBank(NetworkPhase.GAMEPLAY);
		bank.registerC2S(0, PlayerJoinC2SPacket.class, PlayerJoinC2SPacket::new);
		bank.registerS2C(0, WorldChunkS2CPacket.class, WorldChunkS2CPacket::new);
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
		
		private <T extends IPacket<? extends IClientPacketHandler>> void registerS2C(int packetId, Class<T> clazz, Supplier<T> supplier) {
			serverToClientRegistry.register(checkAndCastPacketId(packetId), clazz, supplier);
		}

		private <T extends IPacket<? extends IServerPacketHandler>> void registerC2S(int packetId, Class<T> clazz, Supplier<T> supplier) {
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
