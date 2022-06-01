package mineclone.common.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import mineclone.common.IResource;

public class NetworkManager implements IResource {

	protected final NetworkSide side;
	
	protected final List<INetworkConnection> connections;
	protected final List<INetworkListener> listeners;
	
	public NetworkManager(NetworkSide side) {
		this.side = side;
		
		connections = Collections.synchronizedList(new ArrayList<>());
		listeners = Collections.synchronizedList(new ArrayList<>());
	}

	public void addConnection(INetworkConnection connection) {
		connections.add(connection);
		onConnectionAdded(connection);
	}

	public void removeConnection(INetworkConnection connection) {
		if (connections.remove(connection))
			onConnectionRemoved(connection);
	}
	
	public void update() {
		synchronized (connections) {
			Iterator<INetworkConnection> itr = connections.iterator();
			while (itr.hasNext()) {
				INetworkConnection connection = itr.next();
				if (!connection.isConnected()) {
					itr.remove();
					onConnectionRemoved(connection);
				}
			}
		}
	}
	
	public void addListener(INetworkListener listener) {
		listeners.add(listener);
	}

	public void removeListener(INetworkListener listener) {
		listeners.remove(listener);
	}
	
	private void onConnectionAdded(INetworkConnection connection) {
		synchronized (listeners) {
			for (INetworkListener listener : listeners)
				listener.connectionAdded(connection);
		}
	}
	
	private void onConnectionRemoved(INetworkConnection connection) {
		connection.close();
		
		synchronized (listeners) {
			for (INetworkListener listener : listeners)
				listener.connectionRemoved(connection);
		}
	}

	public void forEach(Consumer<INetworkConnection> action) {
		synchronized (connections) {
			connections.iterator().forEachRemaining(action);
		}
	}

	public NetworkSide getSide() {
		return side;
	}

	@Override
	public void close() {
		synchronized (connections) {
			for (int i = connections.size(); i-- > 0; )
				onConnectionRemoved(connections.remove(i));
		}
	}
}
