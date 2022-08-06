package mineclone.client.renderer.model;

import mineclone.client.graphic.ITextureRegion;
import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.common.world.Direction;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class PoweredBlockModel extends AbstractBlockModel {

	private final ITextureRegion texture;
	private final int color;

	public PoweredBlockModel(ITextureRegion texture, int color) {
		this.texture = texture;
		this.color = color;
	}

	@Override
	public void tessellate(IClientWorld world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
		// FRONT
		addBlockFace(world, pos, builder, Direction.NORTH, texture, color);
		// BACK
		addBlockFace(world, pos, builder, Direction.SOUTH, texture, color);

		// LEFT
		addBlockFace(world, pos, builder, Direction.WEST, texture, color);
		// RIGHT
		addBlockFace(world, pos, builder, Direction.EAST, texture, color);

		// TOP
		addBlockFace(world, pos, builder, Direction.UP, texture, color);
		// BOTTOM
		addBlockFace(world, pos, builder, Direction.DOWN, texture, color);
	}
}
