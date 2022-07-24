package mineclone.server.world.block.signal.wire;

import java.util.Arrays;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.signal.SignalType;
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
	final Boolean[] conductiveSides;

	IBlockPosition pos;
	IBlockState state;
	boolean invalid;

	/** The previous node in the priority queue. */
	Node prev_node;
	/** The next node in the priority queue. */
	Node next_node;
	/** The priority with which this node was queued. */
	int priority;
	/** The wire that queued this node for an update. */
	WireNode neighborWire;

	Node(IServerWorld world) {
		this.world = world;
		this.neighbors = new Node[Directions.ALL.length];
		this.conductiveSides = new Boolean[Directions.ALL.length];
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

		Arrays.fill(conductiveSides, null);

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

	public boolean isConductor(int iDir, SignalType type) {
		Boolean conductive = conductiveSides[iDir];

		if (conductive == null) {
			Direction dir = Directions.ALL[iDir];
			conductive = state.isSignalConductor(dir, type);

			conductiveSides[iDir] = conductive;
		}

		return conductive;
	}

	public boolean isSignalSource(SignalType type) {
		return state.isSignalSource(type);
	}

	public WireNode asWire() {
		throw new UnsupportedOperationException("Not a WireNode!");
	}
}
