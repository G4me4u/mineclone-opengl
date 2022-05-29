package mineclone.common.world.block;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;

public class PlantBlock extends Block {

	public static final IBlockProperty<PlantType> PLANT_TYPE = new EnumBlockProperty<>("type", PlantType.TYPES);
	
	@Override
	public void onStateUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
		if (fromDir == Direction.DOWN && !world.getBlockState(pos.down()).canGrowVegetation())
			world.setBlock(pos, Blocks.AIR_BLOCK, true);
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, PLANT_TYPE);
	}
}
