package mineclone.client.renderer.model;

import static mineclone.client.renderer.world.BlockTextures.ACACIA_PLANKS_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.BIRCH_PLANKS_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.COBBLESTONE_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.DAISY_PLANT_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.DIRT_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.FORGETMENOT_PLANT_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.GRASS_PLANT_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.GRASS_SIDE_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.GRASS_TOP_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.LEAVES_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.MARIGOLD_PLANT_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.OAK_LOG_SIDE_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.OAK_LOG_TOP_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.OAK_PLANKS_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.POWERED_BLOCK_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.STONE_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.WIRE_CROSS_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.WIRE_HLINE_TEXTURE;
import static mineclone.client.renderer.world.BlockTextures.WIRE_VLINE_TEXTURE;

import mineclone.common.util.ColorUtil;
import mineclone.common.util.DebugUtil;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.PlantBlock;
import mineclone.common.world.block.PlantType;
import mineclone.common.world.block.StoneSlabBlock;
import mineclone.common.world.block.StoneType;
import mineclone.common.world.block.WoodPlanksBlock;
import mineclone.common.world.block.WoodPlanksSlabBlock;
import mineclone.common.world.block.WoodType;
import mineclone.common.world.block.signal.wire.Wire;
import mineclone.common.world.block.state.IBlockState;

public final class BlockModelRegistry {

	private static final IBlockModel[] models;
	
	static {
		models = new IBlockModel[Blocks.getMaxIdentifier()];
		
		register(Blocks.AIR_BLOCK, EmptyBlockModel.INSTANCE);
		register(Blocks.DIRT_BLOCK, new BasicBlockModel(DIRT_TEXTURE));
		register(Blocks.GRASS_BLOCK, new BasicBlockModel(GRASS_TOP_TEXTURE, DIRT_TEXTURE, GRASS_SIDE_TEXTURE));
		
		// Models for WoodPlanksBlock
		IBlockState planks = Blocks.PLANKS_BLOCK.getDefaultState();
		register(planks.with(WoodPlanksBlock.WOOD_TYPE, WoodType.OAK), new BasicBlockModel(OAK_PLANKS_TEXTURE));
		register(planks.with(WoodPlanksBlock.WOOD_TYPE, WoodType.BIRCH), new BasicBlockModel(BIRCH_PLANKS_TEXTURE));
		register(planks.with(WoodPlanksBlock.WOOD_TYPE, WoodType.ACACIA), new BasicBlockModel(ACACIA_PLANKS_TEXTURE));
		
		register(Blocks.STONE_BLOCK, new BasicBlockModel(STONE_TEXTURE));
		register(Blocks.COBBLESTONE_BLOCK, new BasicBlockModel(COBBLESTONE_TEXTURE));
		
		// Models for PlantBlock
		IBlockState plant = Blocks.PLANT_BLOCK.getDefaultState();
		register(plant.with(PlantBlock.PLANT_TYPE, PlantType.GRASS), new PlantBlockModel(GRASS_PLANT_TEXTURE));
		register(plant.with(PlantBlock.PLANT_TYPE, PlantType.FORGETMENOT), new CropBlockModel(FORGETMENOT_PLANT_TEXTURE));
		register(plant.with(PlantBlock.PLANT_TYPE, PlantType.MARIGOLD), new PlantBlockModel(MARIGOLD_PLANT_TEXTURE));
		register(plant.with(PlantBlock.PLANT_TYPE, PlantType.DAISY), new PlantBlockModel(DAISY_PLANT_TEXTURE));
		
		register(Blocks.LEAVES_BLOCK, new BasicBlockModel(LEAVES_TEXTURE));
		register(Blocks.LOG_BLOCK, new BasicBlockModel(OAK_LOG_TOP_TEXTURE, OAK_LOG_TOP_TEXTURE, OAK_LOG_SIDE_TEXTURE));
		register(Blocks.REDSTONE_WIRE_BLOCK, new WireBlockModel(WIRE_CROSS_TEXTURE, WIRE_VLINE_TEXTURE, WIRE_HLINE_TEXTURE, (Wire)Blocks.REDSTONE_WIRE_BLOCK, ColorUtil.pack(0xFF, 0x00, 0x00)));
		register(Blocks.BLUESTONE_WIRE_BLOCK, new WireBlockModel(WIRE_CROSS_TEXTURE, WIRE_VLINE_TEXTURE, WIRE_HLINE_TEXTURE, (Wire)Blocks.BLUESTONE_WIRE_BLOCK, ColorUtil.pack(0x00, 0x00, 0xFF)));
		register(Blocks.REDSTONE_BLOCK, new PoweredBlockModel(POWERED_BLOCK_TEXTURE, ColorUtil.pack(0xFF, 0x00, 0x00)));
		register(Blocks.BLUESTONE_BLOCK, new PoweredBlockModel(POWERED_BLOCK_TEXTURE, ColorUtil.pack(0x00, 0x00, 0xFF)));
		
		// Models for StoneSlabBlock
		IBlockState stoneSlab = Blocks.STONE_SLAB_BLOCK.getDefaultState();
		do {
			register(stoneSlab.with(StoneSlabBlock.STONE_TYPE, StoneType.STONE), new SlabBlockModel(STONE_TEXTURE));
			register(stoneSlab.with(StoneSlabBlock.STONE_TYPE, StoneType.COBBLESTONE), new SlabBlockModel(COBBLESTONE_TEXTURE));
		} while ((stoneSlab = stoneSlab.increment(StoneSlabBlock.PLACEMENT)) != Blocks.STONE_SLAB_BLOCK.getDefaultState());

		// Models for WoodPlanksSlabBlock
		IBlockState planksSlab = Blocks.PLANKS_SLAB_BLOCK.getDefaultState();
		do {
			register(planksSlab.with(WoodPlanksSlabBlock.WOOD_TYPE, WoodType.OAK), new SlabBlockModel(OAK_PLANKS_TEXTURE));
			register(planksSlab.with(WoodPlanksSlabBlock.WOOD_TYPE, WoodType.BIRCH), new SlabBlockModel(BIRCH_PLANKS_TEXTURE));
			register(planksSlab.with(WoodPlanksSlabBlock.WOOD_TYPE, WoodType.ACACIA), new SlabBlockModel(ACACIA_PLANKS_TEXTURE));
		} while ((planksSlab = planksSlab.increment(WoodPlanksSlabBlock.PLACEMENT)) != Blocks.PLANKS_SLAB_BLOCK.getDefaultState());
		
		if (DebugUtil.PERFORM_INIT_CHECKS)
			checkStateModels();
	}
	
	private BlockModelRegistry() {
	}

	private static void register(IBlock block, IBlockModel model) {
		IBlockState state = block.getDefaultState();
		do {
			register(state, model);
		} while ((state = state.next()) != block.getDefaultState());
	}

	private static void register(IBlockState state, IBlockModel model) {
		models[Blocks.getIdentifier(state)] = model;
	}

	private static void checkStateModels() {
		for (IBlockState state : Blocks.getStates()) {
			if (getModel(state) == null)
				throw new RuntimeException("Incomplete model registry: " + state.getBlock().getName());
		}
	}
	
	public static IBlockModel getModel(IBlockState state) {
		return models[Blocks.getIdentifier(state)];
	}
}
