package mineclone.server.world.block.signal.wire;

import java.util.Arrays;
import java.util.function.Consumer;

import mineclone.common.world.block.signal.wire.ConnectionSide;
import mineclone.common.world.block.signal.wire.ConnectionType;
import mineclone.server.world.block.signal.wire.WireHandler.NodeProvider;

public class WireConnectionManager {

	/** The owner of these connections. */
	final WireNode owner;

	/** The first connection for each cardinal direction. */
	private final WireConnection[] all;

	private WireConnection head;
	private WireConnection tail;

	/** The total number of connections. */
	int total;

	/**
	 * A 4 bit number that encodes which in direction(s) the owner has connections
	 * to other wires.
	 */
	private int flowTotal;
	/** The direction of flow based connections to other wires. */
	int iFlowDir;

	WireConnectionManager(WireNode owner) {
		this.owner = owner;

		this.all = new WireConnection[ConnectionSide.ALL.length];

		this.total = 0;

		this.flowTotal = 0;
		this.iFlowDir = -1;
	}

	void set(NodeProvider nodes) {
		if (total > 0) {
			clear();
		}

		owner.type.findConnections(owner.world, owner.pos, (side, pos, state, type) -> {
			Node node = nodes.get(pos);

			if (node.isWire()) {
				add(node.asWire(), side, type);
			}

			return true;
		});

		if (total > 0) {
			iFlowDir = WireHandler.FLOW_IN_TO_FLOW_OUT[flowTotal];
		}
	}

	private void clear() {
		Arrays.fill(all, null);

		head = null;
		tail = null;

		total = 0;

		flowTotal = 0;
		iFlowDir = -1;
	}

	private void add(WireNode wire, ConnectionSide side, ConnectionType type) {
		add(new WireConnection(wire, side, type));
	}

	private void add(WireConnection connection) {
		all[connection.side.getIndex()] = connection;

		if (head == null) {
			head = connection;
			tail = connection;
		} else {
			tail.next = connection;
			tail = connection;
		}

		total++;

		flowTotal |= WireHandler.CONNECTION_SIDE_TO_FLOW_IN[connection.side.getIndex()];
	}

	WireConnection get(ConnectionSide side) {
		return all[side.getIndex()];
	}

	/**
	 * Iterate over all connections. Use this method if the iteration order is not
	 * important.
	 */
	void forEach(Consumer<WireConnection> consumer) {
		for (WireConnection c = head; c != null; c = c.next) {
			consumer.accept(c);
		}
	}
}
