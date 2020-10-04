package mineclone.client.renderer.model;

import mineclone.client.graphic.ITextureRegion;
import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.common.world.Direction;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class CropBlockModel extends AbstractBlockModel {

	private static final float PLANT_QUAD_OFFSET = 3.0f / 16.0f;
	
	private final ITextureRegion texture;
	
	public CropBlockModel(ITextureRegion texture) {
		this.texture = texture;
	}
	
	@Override
	public void tessellate(IClientWorld world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
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
