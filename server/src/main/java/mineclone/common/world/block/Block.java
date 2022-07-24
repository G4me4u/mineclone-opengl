package mineclone.common.world.block;

import java.util.List;
import java.util.Random;

import mineclone.common.world.Direction;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.signal.wire.WireType;
import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.IBlockState;

public class Block implements IBlock {

	private final IBlockState defaultState;

	private String name;

	protected Block() {
		name = null;

		defaultState = createDefaultState();
	}

	public void onAdded(IServerWorld world, IBlockPosition pos, IBlockState state) {
	}

	public void onRemoved(IServerWorld world, IBlockPosition pos, IBlockState state) {
	}

	public void onChanged(IServerWorld world, IBlockPosition pos, IBlockState oldState, IBlockState newState) {
	}

	void setName(String name) {
		if (this.name != null)
			throw new IllegalStateException("Name has already been set!");

		this.name = name;
	}

	@Override
	public final String getName() {
		return name;
	}

	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this);
	}

	@Override
	public IBlockState getDefaultState() {
		return defaultState;
	}

	@Override
	public IBlockState getPlacementState(IWorld world, IBlockPosition pos, IBlockState state) {
		return state;
	}

	@Override
	public void updateNeighbors(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.updateNeighbors(pos);
	}

	@Override
	public void updateNeighborShapes(IServerWorld world, IBlockPosition pos, IBlockState state) {
		world.updateNeighborShapes(pos, state);
	}

	@Override
	public void update(IServerWorld world, IBlockPosition pos, IBlockState state) {
	}

	@Override
	public void updateShape(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockPosition neighborPos, IBlockState neighborState) {
	}

	@Override
	public void randomTick(IServerWorld world, IBlockPosition pos, IBlockState state, Random random) {
	}

	@Override
	public boolean doesRandomTicks(IBlockState state) {
		return false;
	}

	@Override
	public void getEntityHitboxes(IWorld world, IBlockPosition pos, IBlockState state, List<EntityHitbox> hitboxes) {
		if (hasEntityHitbox(world, pos, state)) {
			float x = pos.getX();
			float y = pos.getY();
			float z = pos.getZ();

			hitboxes.add(new EntityHitbox(x, y, z, x + 1.0f, y + 1.0f, z + 1.0f));
		}
	}

	protected boolean hasEntityHitbox(IWorld world, IBlockPosition pos, IBlockState state) {
		return isSolid();
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean canGrowVegetation(IBlockState state) {
		return false;
	}

	@Override
	public boolean isAligned(IBlockState state, Direction dir) {
		return isSolid();
	}

	@Override
	public boolean isSignalSource(IBlockState state, SignalType type) {
		return false;
	}

	@Override
	public int getSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, SignalType type) {
		return type.min();
	}

	@Override
	public int getDirectSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, SignalType type) {
		return type.min();
	}

	@Override
	public boolean isAnalogSignalSource(IBlockState state, SignalType type) {
		return false;
	}

	@Override
	public int getAnalogSignal(IServerWorld world, IBlockPosition pos, IBlockState state, SignalType type) {
		return type.min();
	}

	@Override
	public boolean isWire(IBlockState state) {
		return false;
	}

	@Override
	public boolean isWire(IBlockState state, WireType type) {
		return false;
	}

	@Override
	public boolean connectsToWire(IBlockState state, Direction dir) {
		return false;
	}

	@Override
	public boolean isSignalConsumer(IBlockState state, SignalType type) {
		return false;
	}

	@Override
	public boolean isSignalConductor(IBlockState state, Direction face, SignalType type) {
		return isSolid();
	}
}
