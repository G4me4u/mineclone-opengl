package mineclone.common.world.block;

import mineclone.client.renderer.model.BasicBlockModel;
import mineclone.client.renderer.model.IBlockModel;
import mineclone.client.renderer.world.BlockTextures;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;

public class WoodPlanksBlock extends Block {

	public static final IBlockProperty<WoodType> WOOD_TYPE = new EnumBlockProperty<>("type", WoodType.TYPES);
	
	private final IBlockModel[] models;
	
	protected WoodPlanksBlock() {
		models = new IBlockModel[WoodType.TYPES.length];
		
		models[WoodType.OAK.getIndex()] = new BasicBlockModel(BlockTextures.OAK_PLANKS_TEXTURE);
		models[WoodType.BIRCH.getIndex()] = new BasicBlockModel(BlockTextures.BIRCH_PLANKS_TEXTURE);
		models[WoodType.ACACIA.getIndex()] = new BasicBlockModel(BlockTextures.ACACIA_PLANKS_TEXTURE);
	}
	
	@Override
	public IBlockModel getModel(IWorld world, IBlockPosition pos, IBlockState state) {
		return models[state.get(WOOD_TYPE).getIndex()];
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, WOOD_TYPE);
	}
}
