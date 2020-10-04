package mineclone.common.world.block;

import mineclone.client.renderer.world.BlockTextures;
import mineclone.common.world.block.state.IBlockState;

public class DirtBlock extends BasicSolidBlock {
	
	protected DirtBlock() {
		super(BlockTextures.DIRT_TEXTURE);
	}
	
	@Override
	public boolean canGrowVegetation(IBlockState state) {
		return true;
	}
}
