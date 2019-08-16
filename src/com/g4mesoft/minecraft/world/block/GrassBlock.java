package com.g4mesoft.minecraft.world.block;

import java.util.Random;

import com.g4mesoft.minecraft.renderer.BlockTextures;
import com.g4mesoft.minecraft.renderer.tessellator.BasicBlockModel;
import com.g4mesoft.minecraft.renderer.tessellator.IBlockModel;
import com.g4mesoft.minecraft.world.Blocks;
import com.g4mesoft.minecraft.world.Direction;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.state.BlockState;

public class GrassBlock extends Block {

	private final IBlockModel model;
	
	public GrassBlock() {
		model = new BasicBlockModel(BlockTextures.GRASS_TOP_TEXTURE, 
		                            BlockTextures.DIRT_TEXTURE,
		                            BlockTextures.GRASS_SIDE_TEXTURE);
	}
	
	private boolean isValidGrassCondition(World world, IBlockPosition pos) {
		BlockState aboveState = world.getBlockState(pos.getOffset(Direction.UP));
		return !aboveState.getBlock().isSolid();
	}
	
	@Override
	public void randomTick(World world, IBlockPosition pos, BlockState blockState, Random random) {
		if (!isValidGrassCondition(world, pos)) {
			world.setBlock(pos, Blocks.DIRT_BLOCK);
			return;
		}
		
		int x = pos.getX() + random.nextInt(3) - 1;
		int y = pos.getY() + random.nextInt(3) - 1;
		int z = pos.getZ() + random.nextInt(3) - 1;
	
		if (x != pos.getX() || y != pos.getY() || z != pos.getZ()) {
			IBlockPosition dirtPos = new BlockPosition(x, y, z);
			
			BlockState dirtState = world.getBlockState(dirtPos);
			if (dirtState.getBlock() == Blocks.DIRT_BLOCK && isValidGrassCondition(world, dirtPos))
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
