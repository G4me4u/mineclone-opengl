package minecraft.common.world.block;

import java.util.Random;

import minecraft.client.renderer.model.BasicBlockModel;
import minecraft.client.renderer.model.IBlockModel;
import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.IWorld;
import minecraft.common.world.block.state.IBlockState;

public class GrassBlock extends Block {

	private final IBlockModel model;
	
	protected GrassBlock() {
		model = new BasicBlockModel(BlockTextures.GRASS_TOP_TEXTURE, 
		                            BlockTextures.DIRT_TEXTURE,
		                            BlockTextures.GRASS_SIDE_TEXTURE);
	}
	
	@Override
	public IBlockState getPlacementState(IBlockState state, IServerWorld world, IBlockPosition pos) {
		if (isValidGrassCondition(world, pos))
			return state;
		
		return Blocks.DIRT_BLOCK.getDefaultState().getPlacementState(world, pos);
	}
	
	@Override
	public void onBlockUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
		if (fromDir == Direction.UP && !isValidGrassCondition(world, pos))
			world.setBlock(pos, Blocks.DIRT_BLOCK, true);
	}
	
	@Override
	public void onRandomUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Random random) {
		if (!isValidGrassCondition(world, pos)) {
			world.setBlock(pos, Blocks.DIRT_BLOCK, true);
			return;
		}
		
		int xo = random.nextInt(3) - 1;
		int yo = random.nextInt(3) - 1;
		int zo = random.nextInt(3) - 1;
	
		if (xo != 0 && yo != 0 && zo != 0) {
			IBlockPosition dirtPos = pos.offset(xo, yo, zo);
			
			if (world.getBlockState(dirtPos).isOf(Blocks.DIRT_BLOCK) && isValidGrassCondition(world, dirtPos))
				world.setBlock(dirtPos, Blocks.GRASS_BLOCK, true);
		}
	}
	
	private boolean isValidGrassCondition(IWorld world, IBlockPosition pos) {
		return !world.getBlock(pos.up()).isSolid();
	}
	
	@Override
	public boolean hasRandomUpdate() {
		return true;
	}

	@Override
	public IBlockModel getModel(IWorld world, IBlockPosition pos, IBlockState state) {
		return model;
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	public boolean canGrowVegetation(IBlockState state) {
		return true;
	}
}
