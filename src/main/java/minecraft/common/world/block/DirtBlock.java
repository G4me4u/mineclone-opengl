package minecraft.common.world.block;

import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.block.state.BlockState;

public class DirtBlock extends BasicSolidBlock {
	
	protected DirtBlock() {
		super(BlockTextures.DIRT_TEXTURE);
	}
	
	@Override
	public boolean canGrowVegetation(BlockState blockState) {
		return true;
	}
}
