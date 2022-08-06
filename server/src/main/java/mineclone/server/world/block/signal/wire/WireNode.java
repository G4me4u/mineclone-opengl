package mineclone.server.world.block.signal.wire;

import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.signal.wire.ConnectionSide;
import mineclone.common.world.block.signal.wire.IWire;
import mineclone.common.world.block.signal.wire.WireType;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.flags.SetBlockFlags;

/**
 * A WireNode is a Node that represents a wire in the world. It stores all the
 * information about the wire that the WireHandler needs to calculate power
 * changes.
 * 
 * @author Space Walker
 */
public class WireNode extends Node {

	final WireConnectionManager connections;
	final IWire block;
	final WireType type;

	/** The power level this wire currently holds in the world. */
	int currentPower;
	/**
	 * While calculating power changes for a network, this field is used to keep
	 * track of the power level this wire should have.
	 */
	int virtualPower;
	/** The power level received from non-wire components. */
	int externalPower;
	/**
	 * A 4-bit number that keeps track of the power flow of the wires that give this
	 * wire its power level.
	 */
	int flowIn;
	/** The direction of power flow, based on the incoming flow. */
	int iFlowDir;
	boolean added;
	boolean removed;
	boolean shouldBreak;
	boolean root;
	boolean discovered;
	boolean searched;

	/** The next wire in the simple queue. */
	WireNode next_wire;

	WireNode(IServerWorld world, IBlockPosition pos, IBlockState state) {
		super(world);

		this.pos = pos.toImmutable();
		this.state = state;

		this.connections = new WireConnectionManager(this);
		this.block = (IWire)state.getBlock();
		this.type = this.block.getWireType();

		this.virtualPower = this.currentPower = this.block.getSignal(this.state);
		this.priority = priority();
	}

	@Override
	Node set(IBlockPosition pos, IBlockState state, boolean clearNeighbors) {
		throw new UnsupportedOperationException("Cannot update a WireNode!");
	}

	@Override
	int priority() {
		return type.clamp(virtualPower);
	}

	@Override
	int offset() {
		return -type.min();
	}

	@Override
	public boolean isWire() {
		return true;
	}

	@Override
	public boolean isWire(WireType type) {
		return this.type == type;
	}

	@Override
	public WireNode asWire() {
		return this;
	}

	boolean offerPower(int power, ConnectionSide side) {
		if (removed || shouldBreak) {
			return false;
		}
		if (power == virtualPower) {
			flowIn |= WireHandler.CONNECTION_SIDE_TO_FLOW_IN[side.getIndex()];
			return false;
		}
		if (power > virtualPower) {
			virtualPower = power;
			flowIn = WireHandler.CONNECTION_SIDE_TO_FLOW_IN[side.getIndex()];

			return true;
		}

		return false;
	}

	boolean setPower() {
		if (removed) {
			return true;
		}

		state = world.getBlockState(pos);

		if (!state.isOf(block)) {
			return false; // we should never get here
		}

		if (shouldBreak) {
			return world.setBlock(pos, Blocks.AIR_BLOCK);
		}

		currentPower = type.clamp(virtualPower);
		state = block.setSignal(state, currentPower);

		return world.setBlockState(pos, state, SetBlockFlags.NONE);
	}
}
