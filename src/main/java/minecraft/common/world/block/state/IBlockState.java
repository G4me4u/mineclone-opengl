package minecraft.common.world.block.state;

import java.util.List;
import java.util.Random;

import minecraft.client.renderer.model.IBlockModel;
import minecraft.common.world.Direction;
import minecraft.common.world.EntityHitbox;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.World;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.MutableBlockPosition;
import minecraft.server.world.ServerWorld;

public interface IBlockState {

	default public void onAdded(IServerWorld world, IBlockPosition pos) {
		getBlock().onAdded(world, pos, this);
	}
	
	default public void onRemoved(IServerWorld world, IBlockPosition pos) {
		getBlock().onRemoved(world, pos, this);
	}
	
	default public void onStateReplaced(IServerWorld world, IBlockPosition pos) {
		getBlock().onStateReplaced(world, pos, this);
	}
	
	default public void onBlockUpdate(IServerWorld world, IBlockPosition pos, Direction dir, IBlockState sourceState) {
		getBlock().onBlockUpdate(world, pos, this, dir, sourceState);
	}
	
	default public void onStateUpdate(IServerWorld world, IBlockPosition pos, Direction dir, IBlockState sourceState) {
		getBlock().onStateUpdate(world, pos, this, dir, sourceState);
	}
	
	default public void onInventoryUpdate(IServerWorld world, IBlockPosition pos, Direction dir, IBlockState sourceState) {
		getBlock().onInventoryUpdate(world, pos, this, dir, sourceState);
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
	
	default public boolean conductsRedstonePower() {
		return getBlock().conductsRedstonePower(this);
	}
	
	default public boolean connectsToRedstoneWire(Direction dir) {
		return getBlock().connectsToRedstoneWire(this, dir);
	}
	
	default public int getPower(IServerWorld world, IBlockPosition pos, Direction dir, int powerFlags) {
		return getBlock().getPower(this, world, pos, dir, powerFlags);
	}
	
	default public boolean isOf(Block block) {
		return getBlock() == block;
	}
	
	public Block getBlock();
	
	public <T> T getValue(IBlockProperty<T> property);

	public <T> IBlockState withProperty(IBlockProperty<T> property, T value);
	
	public <T> IBlockState incrementProperty(IBlockProperty<T> property);

	public <T> IBlockState decrementProperty(IBlockProperty<T> property);
	
	public IBlockState incrementState();

	public IBlockState decrementState();

}
