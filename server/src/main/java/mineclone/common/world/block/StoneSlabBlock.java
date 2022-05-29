package mineclone.common.world.block;

import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;

public class StoneSlabBlock extends AbstractSlabBlock {

	public static final IBlockProperty<StoneType> STONE_TYPE = new EnumBlockProperty<>("type", StoneType.TYPES);

	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, PLACEMENT, STONE_TYPE);
	}
}
