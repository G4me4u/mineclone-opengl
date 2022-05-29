package mineclone.common.world.block;

import java.util.Random;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.IBlockState;

public class GrassBlock extends Block {

	@Override
	public IBlockState getPlacementState(IWorld world, IBlockPosition pos, IBlockState state) {
		if (isValidGrassCondition(world, pos))
			return state;
		
		return Blocks.DIRT_BLOCK.getDefaultState().getPlacementState(world, pos);
	}
	
	@Override
	public void onStateUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
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
		return !world.getBlockState(pos.up()).isAligned(Direction.DOWN);
	}
	
	@Override
	public boolean hasRandomUpdate(IBlockState state) {
		return true;
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
