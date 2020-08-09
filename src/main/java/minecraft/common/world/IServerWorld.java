package minecraft.common.world;

import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.IBlockState;

public interface IServerWorld extends IWorld {
	
	public static final int NO_FLAGS = 0;

	/* Flags related to block updates */
	public static final int BLOCK_UPDATE_FLAG     = 0b001;
	public static final int STATE_UPDATE_FLAG     = 0b010;
	public static final int INVENTORY_UPDATE_FLAG = 0b100;

	public static final int COMMON_UPDATE_FLAGS = BLOCK_UPDATE_FLAG | STATE_UPDATE_FLAG;
	public static final int ALL_UPDATE_FLAGS    = COMMON_UPDATE_FLAGS | INVENTORY_UPDATE_FLAG;
	
	/* Flags related to redstone power levels */
	public static final int INDIRECT_WEAK_POWER_FLAG   = 0b0001;
	public static final int INDIRECT_STRONG_POWER_FLAG = 0b0010;
	public static final int DIRECT_WEAK_POWER_FLAG     = 0b0100;
	public static final int DIRECT_STRONG_POWER_FLAG   = 0b1000;

	public static final int INDIRECT_POWER_FLAGS     = INDIRECT_WEAK_POWER_FLAG | INDIRECT_STRONG_POWER_FLAG;
	public static final int DIRECT_POWER_FLAGS       = DIRECT_WEAK_POWER_FLAG | DIRECT_STRONG_POWER_FLAG;
	
	public static final int WEAK_POWER_FLAGS         = INDIRECT_WEAK_POWER_FLAG | DIRECT_WEAK_POWER_FLAG;
	public static final int STRONG_POWER_FLAGS       = INDIRECT_STRONG_POWER_FLAG | DIRECT_STRONG_POWER_FLAG;
	
	public static final int ALL_POWER_FLAGS          = INDIRECT_POWER_FLAGS | DIRECT_POWER_FLAGS;
	
	public void generateWorld();
	
	public void growTree(IBlockPosition pos);
	
	public boolean setBlockState(IBlockPosition pos, IBlockState state, boolean updateNeighbors);
	
	public boolean setBlock(IBlockPosition pos, Block block, boolean updateNeighbors);
	
	public void updateNeighbors(IBlockPosition pos, IBlockState state, int updateFlags);
	
	public void updateNeighbor(IBlockPosition pos, Direction fromDir, IBlockState neighborState, int updateFlags);
	
	public int getPower(IBlockPosition pos, int powerFlags);

	public int getPowerExcept(IBlockPosition pos, int powerFlags, Direction exceptDir);
	
}
