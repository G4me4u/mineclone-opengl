package mineclone.common.world.block;

import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;

public class WoodPlanksBlock extends Block {

	public static final IBlockProperty<WoodType> WOOD_TYPE = new EnumBlockProperty<>("type", WoodType.TYPES);
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, WOOD_TYPE);
	}
}
