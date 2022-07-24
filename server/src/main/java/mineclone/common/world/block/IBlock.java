package mineclone.common.world.block;

import java.util.List;
import java.util.Random;

import mineclone.common.world.Direction;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.signal.wire.WireType;
import mineclone.common.world.block.state.IBlockState;

public interface IBlock {

	String getName();

	IBlockState getDefaultState();

	IBlockState getPlacementState(IWorld world, IBlockPosition pos, IBlockState state);

	void onAdded(IServerWorld world, IBlockPosition pos, IBlockState state);

	void onRemoved(IServerWorld world, IBlockPosition pos, IBlockState state);

	void onChanged(IServerWorld world, IBlockPosition pos, IBlockState oldState, IBlockState newState);

	void updateNeighbors(IServerWorld world, IBlockPosition pos, IBlockState state);

	void updateNeighborShapes(IServerWorld world, IBlockPosition pos, IBlockState state);

	void update(IServerWorld world, IBlockPosition pos, IBlockState state);

	void updateShape(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockPosition neighborPos, IBlockState neighborState);

	void randomTick(IServerWorld world, IBlockPosition pos, IBlockState state, Random random);

	boolean doesRandomTicks(IBlockState state);

	void getEntityHitboxes(IWorld world, IBlockPosition pos, IBlockState state, List<EntityHitbox> hitboxes);

	boolean isSolid();

	boolean canGrowVegetation(IBlockState state);

	boolean isAligned(IBlockState state, Direction dir);

	boolean isSignalSource(IBlockState state, SignalType type);

	int getSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, SignalType type);

	int getDirectSignal(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, SignalType type);

	boolean isAnalogSignalSource(IBlockState state, SignalType type);

	int getAnalogSignal(IServerWorld world, IBlockPosition pos, IBlockState state, SignalType type);

	boolean isWire(IBlockState state);

	boolean isWire(IBlockState state, WireType type);

	boolean connectsToWire(IBlockState state, Direction dir);

	boolean isSignalConsumer(IBlockState state, SignalType type);

	boolean isSignalConductor(IBlockState state, Direction face, SignalType type);

}
