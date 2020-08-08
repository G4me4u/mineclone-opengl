package minecraft.common.world.block;

import java.util.Random;

import minecraft.client.renderer.model.BasicBlockModel;
import minecraft.client.renderer.model.IBlockModel;
import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.IWorld;
import minecraft.common.world.block.state.BlockState;
import minecraft.server.world.ServerWorld;

public class GrassBlock extends Block {

	private final IBlockModel model;
	
	protected GrassBlock() {
		model = new BasicBlockModel(BlockTextures.GRASS_TOP_TEXTURE, 
		                            BlockTextures.DIRT_TEXTURE,
		                            BlockTextures.GRASS_SIDE_TEXTURE);
	}
	
	private boolean isValidGrassCondition(IWorld world, IBlockPosition pos) {
		return !world.getBlock(pos.getOffset(Direction.UP)).isSolid();
	}
	
	@Override
	public boolean canGrowVegetation(BlockState blockState) {
		return true;
	}
	
	@Override
	public void randomUpdate(IServerWorld world, IBlockPosition pos, BlockState blockState, Random random) {
		if (!isValidGrassCondition(world, pos)) {
			world.setBlock(pos, Blocks.DIRT_BLOCK, ServerWorld.BLOCK_FLAG + ServerWorld.STATE_FLAG);
			return;
		}
		
		int xo = random.nextInt(3) - 1;
		int yo = random.nextInt(3) - 1;
		int zo = random.nextInt(3) - 1;
	
		if (xo != 0 && yo != 0 && zo != 0) {
			IBlockPosition dirtPos = pos.getOffset(xo, yo, zo);
			if (world.getBlock(dirtPos) == Blocks.DIRT_BLOCK && isValidGrassCondition(world, dirtPos))
				world.setBlock(dirtPos, Blocks.GRASS_BLOCK, ServerWorld.BLOCK_FLAG + ServerWorld.STATE_FLAG);
		}
	}
	
	@Override
	public boolean hasRandomUpdate() {
		return true;
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	public IBlockModel getModel(IWorld world, IBlockPosition pos, BlockState blockState) {
		return model;
	}
	
	@Override
	public void onBlockUpdate(BlockState state, IServerWorld world, IBlockPosition blockPos, Direction direction, BlockState sourceState) {
		if (direction == Direction.UP && !isValidGrassCondition(world, blockPos)) {
			world.setBlock(blockPos, Blocks.DIRT_BLOCK, ServerWorld.BLOCK_FLAG + ServerWorld.STATE_FLAG);
		}
	}
}
