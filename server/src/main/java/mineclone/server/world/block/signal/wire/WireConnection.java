package mineclone.server.world.block.signal.wire;

import mineclone.common.world.block.signal.wire.ConnectionSide;
import mineclone.common.world.block.signal.wire.ConnectionType;

/**
 * This class represents a connection between some WireNode (the 'owner') and a
 * neighboring WireNode. Two wires are considered to be connected if power can
 * flow from one wire to the other (and/or vice versa).
 * 
 * @author Space Walker
 */
public class WireConnection {

	/** The connected wire. */
	final WireNode wire;
	final ConnectionSide side;
	final ConnectionType type;

	/** The next connection in the sequence. */
	WireConnection next;

	WireConnection(WireNode wire, ConnectionSide side, ConnectionType type) {
		this.wire = wire;
		this.side = side;
		this.type = type;
	}
}
