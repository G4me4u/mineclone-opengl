package minecraft.world.block;

import minecraft.renderer.model.BasicBlockModel;
import minecraft.renderer.model.IBlockModel;
import minecraft.renderer.world.BlockTextures;
import minecraft.world.World;
import minecraft.world.block.state.BlockState;
import minecraft.world.block.state.EnumBlockProperty;
import minecraft.world.block.state.IBlockProperty;

public class WoodPlanksBlock extends Block {

	public static final IBlockProperty<WoodType> WOOD_TYPE_PROPERTY = 
			new EnumBlockProperty<WoodType>("type", WoodType.WOOD_TYPES);
	
	private final IBlockModel[] models;
	
	protected WoodPlanksBlock() {
		models = new IBlockModel[WoodType.WOOD_TYPES.length];
		
		models[WoodType.OAK.getIndex()] = new BasicBlockModel(BlockTextures.OAK_PLANKS_TEXTURE);
		models[WoodType.BIRCH.getIndex()] = new BasicBlockModel(BlockTextures.BIRCH_PLANKS_TEXTURE);
		models[WoodType.ACACIA.getIndex()] = new BasicBlockModel(BlockTextures.ACACIA_PLANKS_TEXTURE);
	}
	
	@Override
	public IBlockModel getModel(World world, IBlockPosition pos, BlockState blockState) {
		return models[blockState.getValue(WOOD_TYPE_PROPERTY).getIndex()];
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	protected BlockState createDefaultState() {
		return BlockState.createStateTree(this, WOOD_TYPE_PROPERTY);
	}
}
