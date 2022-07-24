package mineclone.common.world.block.state;

import java.util.List;
import java.util.Random;

import mineclone.common.world.Direction;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.MutableBlockPosition;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.signal.wire.WireType;

public interface IBlockState {

	default IBlockState getPlacementState(IWorld world, IBlockPosition pos) {
		return getBlock().getPlacementState(world, pos, this);
	}

	default void updateNeighbors(IServerWorld world, IBlockPosition pos) {
		getBlock().updateNeighbors(world, pos, this);
	}

	default void updateNeighborShapes(IServerWorld world, IBlockPosition pos) {
		getBlock().updateNeighborShapes(world, pos, this);
	}

	default void update(IServerWorld world, IBlockPosition pos) {
		getBlock().update(world, pos, this);
	}

	default void updateShape(IServerWorld world, IBlockPosition pos, Direction dir, IBlockPosition neighborPos, IBlockState neighborState) {
		getBlock().updateShape(world, pos, this, dir, neighborPos, neighborState);
	}

	default void randomTick(IServerWorld world, MutableBlockPosition pos, Random random) {
		getBlock().randomTick(world, pos, this, random);
	}

	default boolean doesRandomTicks() {
		return getBlock().doesRandomTicks(this);
	}

	default void getEntityHitboxes(IWorld world, MutableBlockPosition pos, List<EntityHitbox> hitboxes) {
		getBlock().getEntityHitboxes(world, pos, this, hitboxes);
	}

	default boolean canGrowVegetation() {
		return getBlock().canGrowVegetation(this);
	}

	default boolean isAligned(Direction dir) {
		return getBlock().isAligned(this, dir);
	}

	default boolean isSignalSource(SignalType type) {
		return getBlock().isSignalSource(this, type);
	}

	default int getSignal(IServerWorld world, IBlockPosition pos, Direction dir, SignalType type) {
		return getBlock().getSignal(world, pos, this, dir, type);
	}

	default int getDirectSignal(IServerWorld world, IBlockPosition pos, Direction dir, SignalType type) {
		return getBlock().getDirectSignal(world, pos, this, dir, type);
	}

	default boolean isAnalogSignalSource(SignalType type) {
		return getBlock().isAnalogSignalSource(this, type);
	}

	default int getAnalogSignal(IServerWorld world, IBlockPosition pos, SignalType type) {
		return getBlock().getAnalogSignal(world, pos, this, type);
	}

	default boolean isWire() {
		return getBlock().isWire(this);
	}

	default boolean isWire(WireType type) {
		return getBlock().isWire(this, type);
	}

	default boolean canConnectToWire(Direction dir) {
		return getBlock().connectsToWire(this, dir);
	}

	default boolean isSignalConsumer(SignalType type) {
		return getBlock().isSignalConsumer(this, type);
	}

	default boolean isSignalConductor(Direction face, SignalType type) {
		return getBlock().isSignalConductor(this, face, type);
	}

	default boolean isAir() {
		return isOf(Blocks.AIR_BLOCK);
	}

	default boolean isOf(IBlock block) {
		return getBlock() == block;
	}

	IBlock getBlock();

	<T> T get(IBlockProperty<T> property);

	<T> IBlockState with(IBlockProperty<T> property, T value);

	<T> IBlockState increment(IBlockProperty<T> property);

	<T> IBlockState decrement(IBlockProperty<T> property);

	IBlockState next();

	IBlockState prev();

}
