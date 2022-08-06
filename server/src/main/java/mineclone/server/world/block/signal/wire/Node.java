package mineclone.server.world.block.signal.wire;

import java.util.Arrays;

import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.signal.wire.WireType;
import mineclone.common.world.block.state.IBlockState;
import mineclone.server.world.block.signal.wire.WireHandler.Directions;

/**
 * A Node represents a block in the world. It also holds a few other pieces of
 * information that speed up the calculations in the WireHandler class.
 * 
 * @author Space Walker
 */
public class Node {

	final IServerWorld world;
	final Node[] neighbors;

	IBlockPosition pos;
	IBlockState state;
	boolean invalid;

	/** The previous node in the priority queue. */
	Node priorityQueue_prev;
	/** The next node in the priority queue. */
	Node priorityQueue_next;
	/** The priority with which this node was queued. */
	int priority;
	/** The wire that queued this node for an update. */
	WireNode neighborWire;

	Node(IServerWorld world) {
		this.world = world;
		this.neighbors = new Node[Directions.ALL.length];
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Node)) {
			return false;
		}

		Node node = (Node)obj;

		return world == node.world && pos.equals(node.pos);
	}

	@Override
	public int hashCode() {
		return pos.hashCode();
	}

	Node set(IBlockPosition pos, IBlockState state, boolean clearNeighbors) {
		if (state.isWire()) {
			throw new IllegalStateException("Cannot update a regular Node to a WireNode!");
		}

		if (clearNeighbors) {
			Arrays.fill(neighbors, null);
		}

		this.pos = pos.toImmutable();
		this.state = state;
		this.invalid = false;

		return this;
	}

	/**
	 * Determine the priority with which this node should be queued.
	 */
	int priority() {
		return neighborWire.priority;
	}

	/**
	 * Determine the offset with which this node should be queued.
	 */
	int offset() {
		return neighborWire.offset();
	}

	public boolean isWire() {
		return false;
	}

	public boolean isWire(WireType type) {
		return false;
	}

	public WireNode asWire() {
		throw new UnsupportedOperationException("Not a WireNode!");
	}
}
