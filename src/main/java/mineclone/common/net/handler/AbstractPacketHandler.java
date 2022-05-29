package mineclone.common.net.handler;

import java.util.function.BiConsumer;

import mineclone.common.TaskScheduler;
import mineclone.common.net.INetworkConnection;
import mineclone.common.net.packet.IPacket;
import mineclone.common.net.packet.PacketRegistry;

public abstract class AbstractPacketHandler implements IPacketHandler {

	protected final INetworkConnection connection;
	
	protected final PacketRegistry<?> packetRegistry;

	public AbstractPacketHandler(INetworkConnection connection, PacketRegistry<?> packetRegistry) {
		this.connection = connection;
		this.packetRegistry = packetRegistry;
	}
	
	@Override
	public void onPacket(IPacket packet) {
		if (packetRegistry.isSynchronous(packet)) {
			getTaskScheduler().schedule(() -> {
				handlePacket(packet, packetRegistry.getHandler(packet));
			});
		} else {
			handlePacket(packet, packetRegistry.getHandler(packet));
		}
	}

	@SuppressWarnings("unchecked")
	private <P extends IPacket, H extends IPacketHandler> void handlePacket(P packet, BiConsumer<H, P> handler) {
		if (handler != null) {
			try {
				// Good cast is guaranteed if the constructor is given
				// the correct packet registry for this particular handler.
				handler.accept((H)this, packet);
			} catch (Throwable t) {
				onThrowable(t);
			}
		}
	}
	
	protected void onThrowable(Throwable t) {
		// TODO: proper exception handling..
		t.printStackTrace();
	}

	protected abstract TaskScheduler getTaskScheduler();
	
	public INetworkConnection getConnection() {
		return connection;
	}
}
