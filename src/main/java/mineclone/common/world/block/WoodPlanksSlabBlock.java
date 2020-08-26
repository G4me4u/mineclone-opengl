package mineclone.common.world.block;

import mineclone.client.renderer.model.IBlockModel;
import mineclone.client.renderer.model.SlabBlockModel;
import mineclone.client.renderer.world.BlockTextures;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;

public class WoodPlanksSlabBlock extends AbstractSlabBlock {

	public static final IBlockProperty<WoodType> WOOD_TYPE = new EnumBlockProperty<>("type", WoodType.TYPES);
	
	private final IBlockModel[] models;
	
	protected WoodPlanksSlabBlock() {
		models = new IBlockModel[WoodType.TYPES.length];
		
		models[WoodType.OAK.getIndex()] = new SlabBlockModel(BlockTextures.OAK_PLANKS_TEXTURE);
		models[WoodType.BIRCH.getIndex()] = new SlabBlockModel(BlockTextures.BIRCH_PLANKS_TEXTURE);
		models[WoodType.ACACIA.getIndex()] = new SlabBlockModel(BlockTextures.ACACIA_PLANKS_TEXTURE);
	}
	
	@Override
	public IBlockModel getModel(IWorld world, IBlockPosition pos, IBlockState state) {
		return models[state.get(WOOD_TYPE).getIndex()];
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, TOP, WOOD_TYPE);
	}
}
