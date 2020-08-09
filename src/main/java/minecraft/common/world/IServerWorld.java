package minecraft.common.world;

import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.IBlockState;

public interface IServerWorld extends IWorld {
	
	public void generateWorld();
	
	public void growTree(IBlockPosition pos);
	
	public void setBlockState(IBlockPosition pos, IBlockState state, boolean updateNeighbors);
	
	public void setBlock(IBlockPosition pos, Block block, boolean updateNeighbors);
	
	public void updateNeighbors(IBlockPosition pos, IBlockState state, int updateFlags);
	
	public void updateNeighbor(IBlockPosition pos, Direction fromDir, IBlockState neighborState, int updateFlags);
	
}
