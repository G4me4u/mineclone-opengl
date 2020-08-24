package minecraft.client.renderer.model;

import minecraft.client.graphic.ITextureRegion;
import minecraft.client.graphic.tessellator.VertexAttribBuilder;
import minecraft.common.world.Direction;
import minecraft.common.world.World;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.IBlockState;

public class BasicBlockModel extends AbstractBlockModel {

	private final ITextureRegion topTexture;
	private final ITextureRegion bottomTexture;
	private final ITextureRegion sideTexture;
	
	public BasicBlockModel(ITextureRegion texture) {
		this(texture, texture, texture);
	}
	
	public BasicBlockModel(ITextureRegion top, ITextureRegion bottom, ITextureRegion side) {
		this.topTexture = top;
		this.bottomTexture = bottom;
		this.sideTexture = side;
	}
	
	@Override
	public void tessellate(World world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
		// FRONT
		addBlockFace(world, pos, builder, Direction.NORTH, sideTexture);
		// BACK
		addBlockFace(world, pos, builder, Direction.SOUTH, sideTexture);
		
		// LEFT
		addBlockFace(world, pos, builder, Direction.WEST, sideTexture);
		// RIGHT
		addBlockFace(world, pos, builder, Direction.EAST, sideTexture);

		// TOP
		addBlockFace(world, pos, builder, Direction.UP, topTexture);
		// BOTTOM
		addBlockFace(world, pos, builder, Direction.DOWN, bottomTexture);
	}
}
