package minecraft.world.block;

import minecraft.graphic.opengl.texture.ITextureRegion;
import minecraft.renderer.model.BasicBlockModel;
import minecraft.renderer.model.IBlockModel;
import minecraft.world.World;
import minecraft.world.block.state.BlockState;

public class BasicSolidBlock extends Block {

	private final IBlockModel model;

	protected BasicSolidBlock(ITextureRegion texture) {
		this(new BasicBlockModel(texture));
	}
	
	protected BasicSolidBlock(IBlockModel model) {
		this.model = model;
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	public IBlockModel getModel(World world, IBlockPosition pos, BlockState blockState) {
		return model;
	}
}
