package minecraft.common.world;

import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.BlockState;

public interface IServerWorld extends IWorld {
	
	public void generateWorld();
	
	public void growTree(IBlockPosition blockPos);
	
	public void setBlockState(IBlockPosition blockPos, BlockState state, int flags);
	
	public void setBlock(IBlockPosition blockPos, Block block, int flags);
	
	public void updateNeighbors(IBlockPosition blockPos, BlockState neighborState, int flags);
}
