package mineclone.common.world.block;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.flags.SetBlockFlags;

public class PlantBlock extends Block {

	public static final IBlockProperty<PlantType> PLANT_TYPE = new EnumBlockProperty<>("type", PlantType.TYPES);
	
	@Override
	public void updateShape(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockPosition neighborPos, IBlockState neighborState) {
		if (dir == Direction.DOWN && !world.getBlockState(pos.down()).canGrowVegetation())
			world.setBlock(pos, Blocks.AIR_BLOCK, SetBlockFlags.UPDATE_NEIGHBOR_SHAPES | SetBlockFlags.UPDATE_NEIGHBORS);
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, PLANT_TYPE);
	}
}
