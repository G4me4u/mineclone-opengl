package mineclone.common.world.block.state;

import java.util.List;
import java.util.Random;

import mineclone.client.renderer.model.IBlockModel;
import mineclone.common.world.Direction;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.Block;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.MutableBlockPosition;

public interface IBlockState {

	default public IBlockState getPlacementState(IWorld world, IBlockPosition pos) {
		return getBlock().getPlacementState(world, pos, this);
	}
	
	default public void onStateUpdate(IServerWorld world, IBlockPosition pos, Direction fromDir, IBlockState fromState) {
		getBlock().onStateUpdate(world, pos, this, fromDir, fromState);
	}
	
	default public void onBlockUpdate(IServerWorld world, IBlockPosition pos, Direction fromDir, IBlockState fromState) {
		getBlock().onBlockUpdate(world, pos, this, fromDir, fromState);
	}
	
	default public void onInventoryUpdate(IServerWorld world, IBlockPosition pos, Direction fromDir, IBlockState fromState) {
		getBlock().onInventoryUpdate(world, pos, this, fromDir, fromState);
	}
	
	default public void onRandomUpdate(IServerWorld world, MutableBlockPosition pos, Random random) {
		getBlock().onRandomUpdate(world, pos, this, random);
	}
	
	default public boolean hasRandomUpdate() {
		return getBlock().hasRandomUpdate(this);
	}
	
	default public void getEntityHitboxes(IWorld world, MutableBlockPosition pos, List<EntityHitbox> hitboxes) {
		getBlock().getEntityHitboxes(world, pos, this, hitboxes);
	}

	default public IBlockModel getModel(IWorld world, MutableBlockPosition pos) {
		return getBlock().getModel(world, pos, this);
	}
	
	default public boolean canGrowVegetation() {
		return getBlock().canGrowVegetation(this);
	}
	
	default public boolean isAligned(Direction dir) {
		return getBlock().isAligned(this, dir);
	}
	
	default public boolean canPowerIndirectly(Direction dir) {
		return getBlock().canPowerIndirectly(this, dir);
	}
	
	default public boolean canConnectToWire(Direction dir) {
		return getBlock().canConnectToWire(this, dir);
	}
	
	default public int getOutputPowerFlags(Direction dir) {
		return getBlock().getOutputPowerFlags(this, dir);
	}

	default public int getPowerTo(IServerWorld world, IBlockPosition pos, Direction dir, int powerFlags) {
		return getBlock().getPowerTo(world, pos, this, dir, powerFlags);
	}
	
	default public boolean isAir() {
		return isOf(Blocks.AIR_BLOCK);
	}
	
	default public boolean isOf(Block block) {
		return getBlock() == block;
	}

	public Block getBlock();
	
	public <T> T get(IBlockProperty<T> property);

	public <T> IBlockState with(IBlockProperty<T> property, T value);
	
	public <T> IBlockState increment(IBlockProperty<T> property);

	public <T> IBlockState decrement(IBlockProperty<T> property);
	
	public IBlockState next();

	public IBlockState prev();

}
