package mineclone.client.renderer.world;

import java.io.IOException;

import mineclone.client.graphic.ITexture;
import mineclone.client.graphic.ITextureRegion;
import mineclone.client.graphic.opengl.TextureLoader;

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
	
	public static final ITextureRegion DIRT_TEXTURE              = getRegion(2, 0);
	public static final ITextureRegion GRASS_TOP_TEXTURE         = getRegion(1, 0);
	public static final ITextureRegion GRASS_SIDE_TEXTURE        = getRegion(0, 0);
	
	public static final ITextureRegion STONE_TEXTURE             = getRegion(0, 1);
	public static final ITextureRegion COBBLESTONE_TEXTURE       = getRegion(1, 1);
	
	public static final ITextureRegion OAK_PLANKS_TEXTURE        = getRegion(0, 2);
	public static final ITextureRegion BIRCH_PLANKS_TEXTURE      = getRegion(1, 2);
	public static final ITextureRegion ACACIA_PLANKS_TEXTURE     = getRegion(2, 2);
	
	public static final ITextureRegion OAK_LOG_SIDE_TEXTURE      = getRegion(0, 3);
	public static final ITextureRegion OAK_LOG_TOP_TEXTURE       = getRegion(1, 3);
	public static final ITextureRegion LEAVES_TEXTURE            = getRegion(2, 3);
	
	public static final ITextureRegion GRASS_PLANT_TEXTURE       = getRegion(3, 0);
	public static final ITextureRegion FORGETMENOT_PLANT_TEXTURE = getRegion(3, 1);
	public static final ITextureRegion MARIGOLD_PLANT_TEXTURE    = getRegion(3, 2);
	public static final ITextureRegion DAISY_PLANT_TEXTURE       = getRegion(3, 3);
	
	public static final ITextureRegion REDSTONE_BLOCK_TEXTURE    = getRegion(0, 4);
	public static final ITextureRegion WIRE_CROSS_TEXTURE        = getRegion(1, 4);
	public static final ITextureRegion WIRE_VLINE_TEXTURE        = getRegion(2, 4);
	public static final ITextureRegion WIRE_HLINE_TEXTURE        = getRegion(3, 4);
	
	private static ITextureRegion getRegion(int sx, int sy) {
		int xs0 = sx * 16;
		int ys0 = sy * 16;
		
		return blocksTexture.getRegion(xs0, ys0, xs0 + 16, ys0 + 16);
	}
	
	public static void bindBlocksTexture() {
		blocksTexture.bind();
	}
}
