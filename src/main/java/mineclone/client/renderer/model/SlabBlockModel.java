package mineclone.client.renderer.model;

import static mineclone.common.world.block.AbstractSlabBlock.TOP;

import mineclone.client.graphic.ITextureRegion;
import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.common.world.Direction;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class SlabBlockModel extends AbstractBlockModel {

	private final ITextureRegion topTexture;
	private final ITextureRegion bottomTexture;
	private final ITextureRegion sideTexture;

	public SlabBlockModel(ITextureRegion texture) {
		this(texture, texture);
	}
	
	public SlabBlockModel(ITextureRegion texture, ITextureRegion sideTexture) {
		this(texture, texture, sideTexture);
	}

	public SlabBlockModel(ITextureRegion topTexture, ITextureRegion bottomTexture, ITextureRegion sideTexture) {
		this.topTexture = topTexture;
		this.bottomTexture = bottomTexture;
		this.sideTexture = sideTexture;
	}
	
	@Override
	public void tessellate(IClientWorld world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
		if (state.get(TOP)) {
			addBlockFace(world, pos, builder, Direction.NORTH, sideTexture, 0.0f, 0.5f, 1.0f, 1.0f);
			addBlockFace(world, pos, builder, Direction.SOUTH, sideTexture, 0.0f, 0.5f, 1.0f, 1.0f);
			addBlockFace(world, pos, builder, Direction.WEST , sideTexture, 0.0f, 0.5f, 1.0f, 1.0f);
			addBlockFace(world, pos, builder, Direction.EAST , sideTexture, 0.0f, 0.5f, 1.0f, 1.0f);
			
			addAlignedQuad(builder, pos, Direction.DOWN, 0.5f, bottomTexture, getLightness(world, pos));
			addBlockFace(world, pos, builder, Direction.UP, topTexture);
		} else {
			addBlockFace(world, pos, builder, Direction.NORTH, sideTexture, 0.0f, 0.0f, 1.0f, 0.5f);
			addBlockFace(world, pos, builder, Direction.SOUTH, sideTexture, 0.0f, 0.0f, 1.0f, 0.5f);
			addBlockFace(world, pos, builder, Direction.WEST , sideTexture, 0.0f, 0.0f, 1.0f, 0.5f);
			addBlockFace(world, pos, builder, Direction.EAST , sideTexture, 0.0f, 0.0f, 1.0f, 0.5f);
			
			addBlockFace(world, pos, builder, Direction.DOWN, bottomTexture);
			addAlignedQuad(builder, pos, Direction.UP, 0.5f, topTexture, getLightness(world, pos));
		}
	}
}
