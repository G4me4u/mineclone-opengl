package minecraft.common.world.block;

import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.block.state.IBlockState;

public class DirtBlock extends BasicSolidBlock {
	
	protected DirtBlock() {
		super(BlockTextures.DIRT_TEXTURE);
	}
	
	@Override
	public boolean canGrowVegetation(IBlockState state) {
		return true;
	}
}
