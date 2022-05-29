package mineclone.client.renderer.model;

import mineclone.client.graphic.ITextureRegion;
import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.common.world.Direction;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class BasicBlockModel extends AbstractBlockModel {

	protected final ITextureRegion topTexture;
	protected final ITextureRegion bottomTexture;
	protected final ITextureRegion sideTexture;
	
	public BasicBlockModel(ITextureRegion texture) {
		this(texture, texture, texture);
	}
	
	public BasicBlockModel(ITextureRegion top, ITextureRegion bottom, ITextureRegion side) {
		this.topTexture = top;
		this.bottomTexture = bottom;
		this.sideTexture = side;
	}
	
	@Override
	public void tessellate(IClientWorld world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
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
