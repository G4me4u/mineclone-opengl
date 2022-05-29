package mineclone.common.world.block;

import java.util.List;
import java.util.Random;

import mineclone.client.renderer.model.IBlockModel;
import mineclone.common.world.Direction;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.IBlockState;

public class Block {

	private String name;
	
	private final IBlockState defaultState;
	
	protected Block() {
		name = null;
		
		defaultState = createDefaultState();
	}
	
	public IBlockState getPlacementState(IWorld world, IBlockPosition pos, IBlockState state) {
		return state;
	}
	
	public void onBlockAdded(IServerWorld world, IBlockPosition pos, IBlockState state) {
		if (!state.isOf(Blocks.AIR_BLOCK))
			world.updateNeighbors(pos, IServerWorld.COMMON_UPDATE_FLAGS);
	}
	
	public void onBlockRemoved(IServerWorld world, IBlockPosition pos, IBlockState state) {
		if (!state.isOf(Blocks.AIR_BLOCK))
			world.updateNeighbors(pos, IServerWorld.COMMON_UPDATE_FLAGS);
	}
	
	public void onStateChanged(IServerWorld world, IBlockPosition pos, IBlockState oldState, IBlockState newState) {
	}
	
	public void onStateUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
	}

	public void onBlockUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
	}
	
	public void onInventoryUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
	}
	
	public void onRandomUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Random random) {
	}
	
	public boolean hasRandomUpdate(IBlockState state) {
		return false;
	}
	
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

	public IBlockModel getModel(IWorld world, IBlockPosition pos, IBlockState state) {
		// TODO: move this out of the common package.
		return null;
	}
	
	public boolean isSolid() {
		return false;
	}
	
	public boolean canGrowVegetation(IBlockState state) {
		return false;
	}
	
	public boolean isAligned(IBlockState state, Direction dir) {
		return isSolid();
	}
	
	public boolean isPowerComponent() {
		return false;
	}
	
	public boolean canPowerIndirectly(IBlockState state, Direction dir) {
		return isSolid();
	}
	
	public boolean canConnectToWire(IBlockState state, Direction dir) {
		return isPowerComponent();
	}
	
	public int getOutputPowerFlags(IBlockState state, Direction dir) {
		return canPowerIndirectly(state, dir) ? IServerWorld.INDIRECT_POWER_FLAGS : IServerWorld.NO_FLAGS;
	}

	public int getPowerTo(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, int powerFlags) {
		if ((powerFlags & IServerWorld.INDIRECT_POWER_FLAGS) != 0 && canPowerIndirectly(state, dir)) {
			if ((powerFlags & IServerWorld.INDIRECT_WEAK_POWER_FLAG) != 0)
				return world.getPowerExceptFrom(pos, dir, IServerWorld.DIRECT_POWER_FLAGS);
			
			return world.getPowerExceptFrom(pos, dir, IServerWorld.DIRECT_STRONG_POWER_FLAG);
		}
		
		return 0;
	}

	Block setName(String name) {
		if (this.name != null)
			throw new IllegalStateException("Name has already been set!");
		
		this.name = name;
	
		return this;
	}
	
	public final String getName() {
		return name;
	}
	
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this);
	}
	
	public IBlockState getDefaultState() {
		return defaultState;
	}
}
