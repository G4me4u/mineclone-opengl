package mineclone.common.net.packet;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import mineclone.common.net.NetworkPhase;
import mineclone.common.net.NetworkSide;
import mineclone.common.net.handler.IClientPacketHandler;
import mineclone.common.net.handler.IPacketHandler;
import mineclone.common.net.handler.IServerPacketHandler;
import mineclone.common.net.packet.c2s.PlayerJoinC2SPacket;
import mineclone.common.net.packet.s2c.ChunkS2CPacket;

public final class PacketRegistries {

	private static final Map<NetworkPhase, RegistryBank<?, ?>> registryBanks = new EnumMap<>(NetworkPhase.class);

	static {
		RegistryBank<IClientPacketHandler, IServerPacketHandler> gameplay = getBank(NetworkPhase.GAMEPLAY);
		gameplay.registerC2S(0, PlayerJoinC2SPacket.class, PlayerJoinC2SPacket::new, IServerPacketHandler::onPlayerJoin, true);
		gameplay.registerS2C(1, ChunkS2CPacket.class, ChunkS2CPacket::new, IClientPacketHandler::onWorldChunk, true);
	}
	
	private PacketRegistries() {
		throw new UnsupportedOperationException();
	}

	public static PacketRegistry<?> getRegistry(NetworkPhase phase, NetworkSide side) {
		return getBank(phase).getRegistry(side);
	}
	
	public static <C extends IClientPacketHandler, S extends IServerPacketHandler> RegistryBank<C, S> getBank(NetworkPhase phase) {
		@SuppressWarnings("unchecked")
		RegistryBank<C, S> bank = (RegistryBank<C, S>)registryBanks.get(phase);
		
		if (bank == null) {
			bank = new RegistryBank<C, S>();
			registryBanks.put(phase, bank);
		}
		
		return bank;
	}
	
	private static class RegistryBank<C extends IPacketHandler, S extends IPacketHandler> {

		private final PacketRegistry<C> s2cPacketRegistry;
		private final PacketRegistry<S> c2sPacketRegistry;
		
		private RegistryBank() {
			s2cPacketRegistry = new PacketRegistry<>();
			c2sPacketRegistry = new PacketRegistry<>();
		}
		
		public <P extends IPacket> void registerS2C(int packetId, Class<P> clazz, Supplier<P> supplier, BiConsumer<C, P> handler, boolean synchronous) {
			s2cPacketRegistry.register(packetId, clazz, supplier, handler, synchronous);
		}

		public <P extends IPacket> void registerC2S(int packetId, Class<P> clazz, Supplier<P> supplier, BiConsumer<S, P> handler, boolean synchronous) {
			c2sPacketRegistry.register(packetId, clazz, supplier, handler, synchronous);
		}
		
		public PacketRegistry<?> getRegistry(NetworkSide receiverSide) {
			switch (receiverSide) {
			case CLIENT:
				return s2cPacketRegistry;
			case SERVER:
				return c2sPacketRegistry;
			}
			
			throw new IllegalStateException("Unknown network side: " + receiverSide);
		}
	}
}
