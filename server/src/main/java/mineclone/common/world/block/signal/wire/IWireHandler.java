package mineclone.common.world.block.signal.wire;

import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public interface IWireHandler {

	/**
	 * This method should be called whenever a wire receives a block update.
	 */
	void onWireUpdate(IBlockPosition pos);

	/**
	 * This method should be called whenever a wire is placed.
	 */
	void onWireAdded(IBlockPosition pos);

	/**
	 * This method should be called whenever a wire is removed.
	 */
	public void onWireRemoved(IBlockPosition pos, IBlockState state);

}
