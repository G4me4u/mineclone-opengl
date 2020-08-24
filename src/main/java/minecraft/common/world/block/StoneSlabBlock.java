package minecraft.common.world.block;

import minecraft.client.renderer.model.IBlockModel;
import minecraft.client.renderer.model.SlabBlockModel;
import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.IWorld;
import minecraft.common.world.block.state.BlockState;
import minecraft.common.world.block.state.EnumBlockProperty;
import minecraft.common.world.block.state.IBlockProperty;
import minecraft.common.world.block.state.IBlockState;

public class StoneSlabBlock extends AbstractSlabBlock {

	public static final IBlockProperty<StoneType> STONE_TYPE = new EnumBlockProperty<>("type", StoneType.TYPES);

	private final IBlockModel[] models;
	
	public StoneSlabBlock() {
		models = new IBlockModel[StoneType.TYPES.length];
		
		models[StoneType.STONE.getIndex()] = new SlabBlockModel(BlockTextures.STONE_TEXTURE);
		models[StoneType.COBBLESTONE.getIndex()] = new SlabBlockModel(BlockTextures.COBBLESTONE_TEXTURE);
	}
	
	@Override
	public IBlockModel getModel(IWorld world, IBlockPosition pos, IBlockState state) {
		return models[state.get(STONE_TYPE).getIndex()];
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, TOP, STONE_TYPE);
	}
}
