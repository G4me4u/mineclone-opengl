package mineclone.common.world.block;

import java.util.List;

import mineclone.common.world.Direction;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.BooleanBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;

public abstract class AbstractSlabBlock extends Block {

	public static final IBlockProperty<Boolean> TOP = new BooleanBlockProperty("top");

	@Override
	public void getEntityHitboxes(IWorld world, IBlockPosition pos, IBlockState state, List<EntityHitbox> hitboxes) {
		float x0 = pos.getX();
		float y0 = state.get(TOP) ? (pos.getY() + 0.5f) : pos.getY();
		float z0 = pos.getZ();
		
		hitboxes.add(new EntityHitbox(x0, y0, z0, x0 + 1.0f, y0 + 0.5f, z0 + 1.0f));
	}
	
	@Override
	protected boolean hasEntityHitbox(IWorld world, IBlockPosition pos, IBlockState state) {
		return true;
	}
	
	@Override
	public boolean isAligned(IBlockState state, Direction dir) {
		return state.get(TOP) ? (dir == Direction.UP) : (dir == Direction.DOWN);
	}
	
	@Override
	protected abstract IBlockState createDefaultState();
	
}
