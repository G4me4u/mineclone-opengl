package mineclone.common.world;

import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.IWorldChunkManager;

public interface IServerWorld extends IWorld {

	public static final int NO_FLAGS = 0;

	/* Flags related to block updates */
	public static final int STATE_UPDATE_FLAG     = 0b001;
	public static final int BLOCK_UPDATE_FLAG     = 0b010;
	public static final int INVENTORY_UPDATE_FLAG = 0b100;

	public static final int COMMON_UPDATE_FLAGS   = STATE_UPDATE_FLAG | BLOCK_UPDATE_FLAG;
	public static final int ALL_UPDATE_FLAGS      = COMMON_UPDATE_FLAGS | INVENTORY_UPDATE_FLAG;

	/* Flags related to redstone power levels */
	public static final int INDIRECT_WEAK_POWER_FLAG   = 0b0001;
	public static final int INDIRECT_STRONG_POWER_FLAG = 0b0010;
	public static final int DIRECT_WEAK_POWER_FLAG     = 0b0100;
	public static final int DIRECT_STRONG_POWER_FLAG   = 0b1000;

	void generateWorld();

	void growTree(IBlockPosition pos);

	boolean setBlockState(IBlockPosition pos, IBlockState newState, boolean updateNeighbors);

	default boolean setBlock(IBlockPosition pos, IBlock newBlock, boolean updateNeighbors) {
		return setBlockState(pos, newBlock.getDefaultState(), updateNeighbors);
	}

	void updateNeighbors(IBlockPosition pos, int updateFlags);

	void updateNeighbor(IBlockPosition pos, Direction fromDir, IBlockState neighborState, int updateFlags);

	default int getPower(IBlockPosition pos) {
		return getPowerExceptFrom(pos, null);
	}

	int getPowerExceptFrom(IBlockPosition pos, Direction exceptDir);

	int getPowerFrom(IBlockPosition pos, Direction dir);

	IWorldChunkManager getChunkManager();

}
