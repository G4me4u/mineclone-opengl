package minecraft.renderer.model;

import minecraft.graphic.opengl.texture.ITextureRegion;
import minecraft.graphic.tessellator.VertexAttribBuilder;
import minecraft.world.Direction;
import minecraft.world.World;
import minecraft.world.block.IBlockPosition;

public class CropBlockModel extends AbstractBlockModel {

	private static final float PLANT_QUAD_OFFSET = 3.0f / 16.0f;
	
	private final ITextureRegion texture;
	
	public CropBlockModel(ITextureRegion texture) {
		this.texture = texture;
	}
	
	@Override
	public void tessellate(World world, IBlockPosition pos, VertexAttribBuilder builder) {
		float lightness = getLightness(world, pos);
		
		addAlignedQuad(builder, pos, Direction.NORTH, PLANT_QUAD_OFFSET, texture, lightness);
		addAlignedQuad(builder, pos, Direction.SOUTH, PLANT_QUAD_OFFSET, texture, lightness);
		addAlignedQuad(builder, pos, Direction.WEST , PLANT_QUAD_OFFSET, texture, lightness);
		addAlignedQuad(builder, pos, Direction.EAST , PLANT_QUAD_OFFSET, texture, lightness);

		// Add opposite faces
		addAlignedQuad(builder, pos, Direction.NORTH, 1.0f - PLANT_QUAD_OFFSET, texture, lightness);
		addAlignedQuad(builder, pos, Direction.SOUTH, 1.0f - PLANT_QUAD_OFFSET, texture, lightness);
		addAlignedQuad(builder, pos, Direction.WEST , 1.0f - PLANT_QUAD_OFFSET, texture, lightness);
		addAlignedQuad(builder, pos, Direction.EAST , 1.0f - PLANT_QUAD_OFFSET, texture, lightness);
	}
}
