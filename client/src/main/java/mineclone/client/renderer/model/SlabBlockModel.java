package mineclone.client.renderer.model;

import static mineclone.common.world.block.AbstractSlabBlock.PLACEMENT;

import mineclone.client.graphic.ITextureRegion;
import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.common.world.Direction;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class SlabBlockModel extends BasicBlockModel {

	public SlabBlockModel(ITextureRegion texture) {
		this(texture, texture);
	}
	
	public SlabBlockModel(ITextureRegion topBottom, ITextureRegion side) {
		this(topBottom, topBottom, side);
	}

	public SlabBlockModel(ITextureRegion top, ITextureRegion bottom, ITextureRegion side) {
		super(top, bottom, side);
	}
	
	@Override
	public void tessellate(IClientWorld world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
		switch (state.get(PLACEMENT)) {
		case TOP:
			addBlockFace(world, pos, builder, Direction.NORTH, sideTexture, 0.0f, 0.5f, 1.0f, 1.0f);
			addBlockFace(world, pos, builder, Direction.SOUTH, sideTexture, 0.0f, 0.5f, 1.0f, 1.0f);
			addBlockFace(world, pos, builder, Direction.WEST , sideTexture, 0.0f, 0.5f, 1.0f, 1.0f);
			addBlockFace(world, pos, builder, Direction.EAST , sideTexture, 0.0f, 0.5f, 1.0f, 1.0f);
			
			addAlignedQuad(builder, pos, Direction.DOWN, 0.5f, bottomTexture, getLightness(world, pos));
			addBlockFace(world, pos, builder, Direction.UP, topTexture);
			break;
		case BOTTOM:
			addBlockFace(world, pos, builder, Direction.NORTH, sideTexture, 0.0f, 0.0f, 1.0f, 0.5f);
			addBlockFace(world, pos, builder, Direction.SOUTH, sideTexture, 0.0f, 0.0f, 1.0f, 0.5f);
			addBlockFace(world, pos, builder, Direction.WEST , sideTexture, 0.0f, 0.0f, 1.0f, 0.5f);
			addBlockFace(world, pos, builder, Direction.EAST , sideTexture, 0.0f, 0.0f, 1.0f, 0.5f);
			
			addBlockFace(world, pos, builder, Direction.DOWN, bottomTexture);
			addAlignedQuad(builder, pos, Direction.UP, 0.5f, topTexture, getLightness(world, pos));
			break;
		case BOTH:
			super.tessellate(world, pos, state, builder);
			break;
		default:
			break;
		}
	}
}
