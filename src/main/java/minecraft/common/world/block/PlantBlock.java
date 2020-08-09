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
import minecraft.common.world.block.state.IBlockState;

public class PlantBlock extends Block {

	public static final IBlockProperty<PlantType> PLANT_TYPE_PROPERTY = new EnumBlockProperty<>("type", PlantType.TYPES);
	
	private final IBlockModel[] models;
	
	public PlantBlock() {
		models = new IBlockModel[PlantType.TYPES.length];
		
		models[PlantType.GRASS.getIndex()] = new PlantBlockModel(BlockTextures.GRASS_PLANT_TEXTURE);
		models[PlantType.FORGETMENOT.getIndex()] = new CropBlockModel(BlockTextures.FORGETMENOT_PLANT_TEXTURE);
		models[PlantType.MARIGOLD.getIndex()] = new PlantBlockModel(BlockTextures.MARIGOLD_PLANT_TEXTURE);
		models[PlantType.DAISY.getIndex()] = new PlantBlockModel(BlockTextures.DAISY_PLANT_TEXTURE);
	}

	@Override
	public void onBlockUpdate(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir, IBlockState sourceState) {
		if (dir == Direction.DOWN && !world.getBlockState(pos.getOffset(dir)).canGrowVegetation())
			world.setBlock(pos, Blocks.AIR_BLOCK, true);
	}
	
	@Override
	public IBlockModel getModel(IWorld world, IBlockPosition pos, IBlockState state) {
		return models[state.getValue(PLANT_TYPE_PROPERTY).getIndex()];
	}
	
	@Override
	protected IBlockState createDefaultState() {
		return BlockState.createStateTree(this, PLANT_TYPE_PROPERTY);
	}
}
