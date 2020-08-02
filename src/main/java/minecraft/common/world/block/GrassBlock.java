package minecraft.common.world.block;

import java.util.Random;

import minecraft.client.renderer.model.BasicBlockModel;
import minecraft.client.renderer.model.IBlockModel;
import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.World;
import minecraft.common.world.block.state.BlockState;

public class GrassBlock extends Block {

	private final IBlockModel model;
	
	protected GrassBlock() {
		model = new BasicBlockModel(BlockTextures.GRASS_TOP_TEXTURE, 
		                            BlockTextures.DIRT_TEXTURE,
		                            BlockTextures.GRASS_SIDE_TEXTURE);
	}
	
	private boolean isValidGrassCondition(World world, IBlockPosition pos) {
		return !world.getBlock(pos.getOffset(Direction.UP)).isSolid();
	}
	
	@Override
	public void randomTick(World world, IBlockPosition pos, BlockState blockState, Random random) {
		if (!isValidGrassCondition(world, pos)) {
			world.setBlock(pos, Blocks.DIRT_BLOCK);
			return;
		}
		
		int xo = random.nextInt(3) - 1;
		int yo = random.nextInt(3) - 1;
		int zo = random.nextInt(3) - 1;
	
		if (xo != 0 && yo != 0 && zo != 0) {
			IBlockPosition dirtPos = pos.getOffset(xo, yo, zo);
			if (world.getBlock(dirtPos) == Blocks.DIRT_BLOCK && isValidGrassCondition(world, dirtPos))
				world.setBlock(dirtPos, Blocks.GRASS_BLOCK);
		}
	}
	
	@Override
	public boolean isRandomTicked() {
		return true;
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
