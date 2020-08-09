package minecraft.common.world.block;

import minecraft.client.renderer.model.CropBlockModel;
import minecraft.client.renderer.model.IBlockModel;
import minecraft.client.renderer.model.PlantBlockModel;
import minecraft.client.renderer.world.BlockTextures;
import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.IWorld;
import minecraft.common.world.block.state.BlockState;
import minecraft.common.world.block.state.EnumBlockProperty;
import minecraft.common.world.block.state.IBlockProperty;

public class PlantBlock extends Block {

	public static final IBlockProperty<PlantType> PLANT_TYPE_PROPERTY = new EnumBlockProperty<>("type", PlantType.PLANT_TYPES);
	
	private final IBlockModel[] models;
	
	public PlantBlock() {
		models = new IBlockModel[PlantType.PLANT_TYPES.length];
		
		models[PlantType.GRASS.getIndex()] = new PlantBlockModel(BlockTextures.GRASS_PLANT_TEXTURE);
		models[PlantType.FORGETMENOT.getIndex()] = new CropBlockModel(BlockTextures.FORGETMENOT_PLANT_TEXTURE);
		models[PlantType.MARIGOLD.getIndex()] = new PlantBlockModel(BlockTextures.MARIGOLD_PLANT_TEXTURE);
		models[PlantType.DAISY.getIndex()] = new PlantBlockModel(BlockTextures.DAISY_PLANT_TEXTURE);
	}
	
	@Override
	public IBlockModel getModel(IWorld world, IBlockPosition pos, BlockState blockState) {
		return models[blockState.getValue(PLANT_TYPE_PROPERTY).getIndex()];
	}
	
	@Override
	protected BlockState createDefaultState() {
		return BlockState.createStateTree(this, PLANT_TYPE_PROPERTY);
	}
	
	@Override
	public void onBlockUpdate(BlockState state, IServerWorld world, IBlockPosition blockPos, Direction direction, BlockState sourceState) {
		if (direction == Direction.DOWN) {
			if (!world.getBlockState(blockPos.getOffset(direction)).canGrowVegetation()) {
				world.setBlock(blockPos, Blocks.AIR_BLOCK, true);
			}
		}
	}
}
