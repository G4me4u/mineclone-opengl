package minecraft.client.renderer.model;

import minecraft.client.graphic.ITextureRegion;
import minecraft.client.graphic.tessellator.VertexAttribBuilder;
import minecraft.common.world.Direction;
import minecraft.common.world.World;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.IBlockState;

import static minecraft.common.world.block.AbstractSlabBlock.TOP;

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
	public void tessellate(World world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
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
