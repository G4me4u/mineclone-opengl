package mineclone.common.world.block;

import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.IBlockState;

public class Block implements IBlock {

	private final IBlockState defaultState;

	private String name;

	protected Block() {
		name = null;

		defaultState = createDefaultState();
	}

	void setName(String name) {
		if (this.name != null)
			throw new IllegalStateException("Name has already been set!");

		this.name = name;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final IBlockState getDefaultState() {
		return defaultState;
	}

	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this);
	}
}
