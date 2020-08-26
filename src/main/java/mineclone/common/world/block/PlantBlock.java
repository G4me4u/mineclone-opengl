package mineclone.common.world.block;

import mineclone.client.renderer.model.CropBlockModel;
import mineclone.client.renderer.model.IBlockModel;
import mineclone.client.renderer.model.PlantBlockModel;
import mineclone.client.renderer.world.BlockTextures;
import mineclone.common.world.Blocks;
import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.state.BlockState;
import mineclone.common.world.block.state.EnumBlockProperty;
import mineclone.common.world.block.state.IBlockProperty;
import mineclone.common.world.block.state.IBlockState;

public class PlantBlock extends Block {

	public static final IBlockProperty<PlantType> PLANT_TYPE = new EnumBlockProperty<>("type", PlantType.TYPES);
	
	private final IBlockModel[] models;
	
	public PlantBlock() {
		models = new IBlockModel[PlantType.TYPES.length];
		
		models[PlantType.GRASS.getIndex()] = new PlantBlockModel(BlockTextures.GRASS_PLANT_TEXTURE);
		models[PlantType.FORGETMENOT.getIndex()] = new CropBlockModel(BlockTextures.FORGETMENOT_PLANT_TEXTURE);
		models[PlantType.MARIGOLD.getIndex()] = new PlantBlockModel(BlockTextures.MARIGOLD_PLANT_TEXTURE);
		models[PlantType.DAISY.getIndex()] = new PlantBlockModel(BlockTextures.DAISY_PLANT_TEXTURE);
	}

	@Override
	public void onStateUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir, IBlockState fromState) {
		if (fromDir == Direction.DOWN && !world.getBlockState(pos.down()).canGrowVegetation())
			world.setBlock(pos, Blocks.AIR_BLOCK, true);
	}
	
	@Override
	public IBlockModel getModel(IWorld world, IBlockPosition pos, IBlockState state) {
		return models[state.get(PLANT_TYPE).getIndex()];
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, PLANT_TYPE);
	}
}
