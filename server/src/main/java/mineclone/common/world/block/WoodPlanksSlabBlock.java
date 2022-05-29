package mineclone.common.world.block;

import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;

public class WoodPlanksSlabBlock extends AbstractSlabBlock {

	public static final IBlockProperty<WoodType> WOOD_TYPE = new EnumBlockProperty<>("type", WoodType.TYPES);
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, PLACEMENT, WOOD_TYPE);
	}
}
