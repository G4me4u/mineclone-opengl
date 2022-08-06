package mineclone.common.world.block.state;

import java.util.List;
import java.util.Random;

import mineclone.common.world.Direction;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.MutableBlockPosition;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.signal.wire.ConnectionSide;
import mineclone.common.world.block.signal.wire.WireType;

public interface IBlockState {

	default boolean isOf(IBlock block) {
		return getBlock() == block;
	}

	default boolean isAir() {
		return getBlock().isAir();
	}

	default IBlockState getPlacementState(IWorld world, IBlockPosition pos) {
		return getBlock().getPlacementState(world, pos, this);
	}

	default boolean canExist(IWorld world, IBlockPosition pos) {
		return getBlock().canExist(world, pos, this);
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

	default void randomUpdate(IServerWorld world, MutableBlockPosition pos, Random random) {
		getBlock().randomUpdate(world, pos, this, random);
	}

	default boolean doesRandomUpdates() {
		return getBlock().doesRandomUpdates(this);
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
		return getBlock().isSignalSource(type);
	}

	default int getSignal(IServerWorld world, IBlockPosition pos, Direction dir, SignalType type) {
		return getBlock().getSignal(world, pos, this, dir, type);
	}

	default int getDirectSignal(IServerWorld world, IBlockPosition pos, Direction dir, SignalType type) {
		return getBlock().getDirectSignal(world, pos, this, dir, type);
	}

	default boolean isWire() {
		return getBlock().isWire();
	}

	default boolean isWire(SignalType type) {
		return getBlock().isWire(type);
	}

	default boolean isWire(WireType type) {
		return getBlock().isWire(type);
	}

	default boolean shouldWireConnect(IWorld world, IBlockPosition pos, ConnectionSide side, WireType type) {
		return getBlock().shouldWireConnect(world, pos, this, side, type);
	}

	default boolean isSignalConsumer(SignalType type) {
		return getBlock().isSignalConsumer(type);
	}

	default boolean isSignalConductor(Direction dir, SignalType type) {
		return getBlock().isSignalConductor(this, dir, type);
	}

	IBlock getBlock();

	<T> T get(IBlockProperty<T> property);

	<T> IBlockState with(IBlockProperty<T> property, T value);

	<T> IBlockState increment(IBlockProperty<T> property);

	<T> IBlockState decrement(IBlockProperty<T> property);

	IBlockState next();

	IBlockState prev();

}
