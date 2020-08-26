package mineclone.common.world.block;

import mineclone.client.renderer.model.IBlockModel;
import mineclone.client.renderer.model.SlabBlockModel;
import mineclone.client.renderer.world.BlockTextures;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;

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
		return BlockState.createStateTree(this, PLACEMENT, STONE_TYPE);
	}
}
