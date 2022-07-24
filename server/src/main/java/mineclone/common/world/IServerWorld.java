package mineclone.common.world;

import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.IWorldChunkManager;
import mineclone.server.world.block.signal.wire.WireHandler;

public interface IServerWorld extends IWorld {

	void generateWorld();

	void growTree(IBlockPosition pos);

	boolean setBlockState(IBlockPosition pos, IBlockState newState, int flags);

	default boolean setBlock(IBlockPosition pos, IBlock newBlock, int flags) {
		return setBlockState(pos, newBlock.getDefaultState(), flags);
	}

	default void updateNeighbors(IBlockPosition pos) {
		updateNeighborsExceptFrom(pos, null);
	}

	void updateNeighborsExceptFrom(IBlockPosition pos, Direction exceptDir);

	void updateNeighbor(IBlockPosition pos);

	default void updateNeighborShapes(IBlockPosition pos, IBlockState state) {
		updateNeighborShapesExceptFrom(pos, state, null);
	}

	void updateNeighborShapesExceptFrom(IBlockPosition pos, IBlockState state, Direction exceptDir);

	void updateNeighborShape(IBlockPosition pos, Direction dir, IBlockPosition neighborPos, IBlockState neighborState);

	default int getSignal(IBlockPosition pos, SignalType type) {
		return getSignalExceptFrom(pos, null, type);
	}

	int getSignalExceptFrom(IBlockPosition pos, Direction exceptDir, SignalType type);

	int getSignalFrom(IBlockPosition pos, Direction dir, SignalType type);

	default int getDirectSignal(IBlockPosition pos, SignalType type) {
		return getDirectSignalExceptFrom(pos, null, type);
	}

	int getDirectSignalExceptFrom(IBlockPosition pos, Direction exceptDir, SignalType type);

	int getDirectSignalFrom(IBlockPosition pos, Direction dir, SignalType type);

	int getAnalogSignalFrom(IBlockPosition pos, Direction dir, SignalType type);

	WireHandler getWireHandler();

	IWorldChunkManager getChunkManager();

}
