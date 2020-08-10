package minecraft.common.world.block.state;

import java.util.List;
import java.util.Random;

import minecraft.client.renderer.model.IBlockModel;
import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.EntityHitbox;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.World;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.MutableBlockPosition;
import minecraft.server.world.ServerWorld;

public interface IBlockState {

	default public IBlockState getPlacementState(IServerWorld world, IBlockPosition pos) {
		return getBlock().getPlacementState(this, world, pos);
	}
	
	default public void onAdded(IServerWorld world, IBlockPosition pos) {
		getBlock().onAdded(world, pos, this);
	}
	
	default public void onRemoved(IServerWorld world, IBlockPosition pos) {
		getBlock().onRemoved(world, pos, this);
	}
	
	default public void onStateReplaced(IServerWorld world, IBlockPosition pos) {
		getBlock().onStateReplaced(world, pos, this);
	}
	
	default public void onBlockUpdate(IServerWorld world, IBlockPosition pos, Direction fromDir, IBlockState fromState) {
		getBlock().onBlockUpdate(world, pos, this, fromDir, fromState);
	}
	
	default public void onStateUpdate(IServerWorld world, IBlockPosition pos, Direction fromDir, IBlockState fromState) {
		getBlock().onStateUpdate(world, pos, this, fromDir, fromState);
	}
	
	default public void onInventoryUpdate(IServerWorld world, IBlockPosition pos, Direction fromDir, IBlockState fromState) {
		getBlock().onInventoryUpdate(world, pos, this, fromDir, fromState);
	}
	
	default public void onRandomUpdate(ServerWorld world, MutableBlockPosition pos, Random random) {
		getBlock().onRandomUpdate(world, pos, this, random);
	}
	
	default public void getEntityHitboxes(World world, MutableBlockPosition pos, List<EntityHitbox> hitboxes) {
		getBlock().getEntityHitboxes(world, pos, this, hitboxes);
	}

	default public IBlockModel getModel(World world, MutableBlockPosition pos) {
		return getBlock().getModel(world, pos, this);
	}
	
	default public boolean canGrowVegetation() {
		return getBlock().canGrowVegetation(this);
	}
	
	default public boolean isAligned(Direction dir) {
		return getBlock().hasAligned(this, dir);
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
		return getBlock().getPowerTo(this, world, pos, dir, powerFlags);
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
