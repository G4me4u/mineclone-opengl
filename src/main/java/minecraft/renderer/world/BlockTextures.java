package minecraft.renderer.world;

import java.io.IOException;

import minecraft.graphic.opengl.texture.ITexture;
import minecraft.graphic.opengl.texture.ITextureRegion;
import minecraft.graphic.opengl.texture.TextureLoader;

public class BlockTextures {

	private static final String BLOCKS_TEXTURE_FILE = "/assets/textures/blocks.png";
	
	public static final ITexture blocksTexture;
	
	static {
		try {
			// TODO: Add a proper asset manager.
			blocksTexture = TextureLoader.loadTexture(BLOCKS_TEXTURE_FILE);
		} catch (IOException e) {
			throw new RuntimeException("Unable to load blocks texture", e);
		}
	}
	
	public static final ITextureRegion DIRT_TEXTURE              = blocksTexture.getRegion(0.50f, 0.75f, 0.75f, 1.00f);
	public static final ITextureRegion GRASS_TOP_TEXTURE         = blocksTexture.getRegion(0.25f, 0.75f, 0.50f, 1.00f);
	public static final ITextureRegion GRASS_SIDE_TEXTURE        = blocksTexture.getRegion(0.00f, 0.75f, 0.25f, 1.00f);
	
	public static final ITextureRegion STONE_TEXTURE             = blocksTexture.getRegion(0.00f, 0.50f, 0.25f, 0.75f);
	public static final ITextureRegion COBBLESTONE_TEXTURE       = blocksTexture.getRegion(0.25f, 0.50f, 0.50f, 0.75f);
	
	public static final ITextureRegion OAK_PLANKS_TEXTURE        = blocksTexture.getRegion(0.00f, 0.25f, 0.25f, 0.50f);
	public static final ITextureRegion BIRCH_PLANKS_TEXTURE      = blocksTexture.getRegion(0.25f, 0.25f, 0.50f, 0.50f);
	public static final ITextureRegion ACACIA_PLANKS_TEXTURE     = blocksTexture.getRegion(0.50f, 0.25f, 0.75f, 0.50f);
	
	public static final ITextureRegion OAK_LOG_SIDE_TEXTURE      = blocksTexture.getRegion(0.00f, 0.00f, 0.25f, 0.25f);
	public static final ITextureRegion OAK_LOG_TOP_TEXTURE       = blocksTexture.getRegion(0.25f, 0.00f, 0.50f, 0.25f);
	public static final ITextureRegion LEAVES_TEXTURE            = blocksTexture.getRegion(0.50f, 0.00f, 0.75f, 0.25f);
	
	public static final ITextureRegion GRASS_PLANT_TEXTURE       = blocksTexture.getRegion(0.75f, 0.75f, 1.00f, 1.00f);
	public static final ITextureRegion FORGETMENOT_PLANT_TEXTURE = blocksTexture.getRegion(0.75f, 0.50f, 1.00f, 0.75f);
	public static final ITextureRegion MARIGOLD_PLANT_TEXTURE    = blocksTexture.getRegion(0.75f, 0.25f, 1.00f, 0.50f);
	public static final ITextureRegion DAISY_PLANT_TEXTURE       = blocksTexture.getRegion(0.75f, 0.00f, 1.00f, 0.25f);
	
	public static void bindBlocksTexture() {
		blocksTexture.bind();
	}
}
