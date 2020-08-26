package mineclone.common.world.block;

import java.util.List;

import mineclone.common.world.Direction;
import mineclone.common.world.EntityHitbox;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;

public abstract class AbstractSlabBlock extends Block {

	public static final IBlockProperty<SlabPlacement> PLACEMENT = new EnumBlockProperty<SlabPlacement>("placement", SlabPlacement.PLACEMENTS);

	@Override
	public void getEntityHitboxes(IWorld world, IBlockPosition pos, IBlockState state, List<EntityHitbox> hitboxes) {
		SlabPlacement placement = state.get(PLACEMENT);
		
		if (placement == SlabPlacement.BOTH) {
			super.getEntityHitboxes(world, pos, state, hitboxes);
		} else {
			float x0 = pos.getX();
			float y0 = pos.getY();
			float z0 = pos.getZ();
			
			if (placement == SlabPlacement.TOP)
				y0 += 0.5f;
			
			hitboxes.add(new EntityHitbox(x0, y0, z0, x0 + 1.0f, y0 + 0.5f, z0 + 1.0f));
		}
	}
	
	@Override
	protected boolean hasEntityHitbox(IWorld world, IBlockPosition pos, IBlockState state) {
		return true;
	}
	
	@Override
	public boolean isAligned(IBlockState state, Direction dir) {
		switch (state.get(PLACEMENT)) {
		case TOP:
			return (dir == Direction.UP);
		case BOTTOM:
			return (dir == Direction.DOWN);
		case BOTH:
			return true;
		}

		throw new IllegalStateException("Unknown slab placement");
	}
	
	@Override
	protected abstract IBlockState createDefaultState();
	
}
