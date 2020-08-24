package minecraft.common.world.block;

import minecraft.client.renderer.model.BasicBlockModel;
import minecraft.client.renderer.model.IBlockModel;
import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.IWorld;
import minecraft.common.world.block.state.IBlockState;

public class RedstoneBlock extends Block {
	
	private final IBlockModel model;
	
	public RedstoneBlock() {
		model = new BasicBlockModel(BlockTextures.REDSTONE_BLOCK_TEXTURE);
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
	public boolean canPowerIndirectly(IBlockState state, Direction dir) {
		return false;
	}
	
	@Override
	public boolean canConnectToWire(IBlockState state, Direction dir) {
		return true;
	}
	
	@Override
	public int getOutputPowerFlags(IBlockState state, Direction dir) {
		return IServerWorld.INDIRECT_STRONG_POWER_FLAG;
	}
	
	@Override
	public int getPowerTo(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, int powerFlags) {
		return (powerFlags & getOutputPowerFlags(state, dir)) == 0 ? 0 : 15;
	}
}
