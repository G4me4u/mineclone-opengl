package mineclone.common.world.block;

import java.util.List;
import java.util.Random;

import mineclone.common.world.Direction;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
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
		if (!state.isAir())
			world.updateNeighbors(pos, IServerWorld.COMMON_UPDATE_FLAGS);
	}

	public void onRemoved(IServerWorld world, IBlockPosition pos, IBlockState state) {
		if (!state.isAir())
			world.updateNeighbors(pos, IServerWorld.COMMON_UPDATE_FLAGS);
	}

	public void onChanged(IServerWorld world, IBlockPosition pos, IBlockState oldState, IBlockState newState) {
	}

	@Override
	public IBlockState getPlacementState(IWorld world, IBlockPosition pos, IBlockState state) {
		return state;
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
}
