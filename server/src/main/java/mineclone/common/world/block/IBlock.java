package mineclone.common.world.block;

import java.util.List;
import java.util.Random;

import mineclone.common.world.Direction;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.IBlockState;

public interface IBlock {

	IBlockState getPlacementState(IWorld world, IBlockPosition pos, IBlockState state);

	void onAdded(IServerWorld world, IBlockPosition pos, IBlockState state);

	void onRemoved(IServerWorld world, IBlockPosition pos, IBlockState state);

	void onChanged(IServerWorld world, IBlockPosition pos, IBlockState oldState, IBlockState newState);

	void update(IServerWorld world, IBlockPosition pos, IBlockState state);

	void updateShape(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockPosition neighborPos, IBlockState neighborState);

	void randomTick(IServerWorld world, IBlockPosition pos, IBlockState state, Random random);

	boolean doesRandomTicks(IBlockState state);

	void getEntityHitboxes(IWorld world, IBlockPosition pos, IBlockState state, List<EntityHitbox> hitboxes);

	boolean isSolid();

	boolean canGrowVegetation(IBlockState state);

	boolean isAligned(IBlockState state, Direction dir);

	String getName();

	IBlockState getDefaultState();

}
