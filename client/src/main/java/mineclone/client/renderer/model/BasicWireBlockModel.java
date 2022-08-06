package mineclone.client.renderer.model;

import mineclone.client.graphic.ITextureRegion;
import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.common.world.Axis;
import mineclone.common.world.Direction;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.block.BasicWireBlock;
import mineclone.common.world.block.BasicWireBlock.WireSide;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class BasicWireBlockModel extends WireBlockModel {

	private static final float WIRE_OFFSET = 0.005f;
	private static final float SIDE_CUT_OFFSET = 5.0f / 16.0f;

	private final ITextureRegion crossTexture;
	private final ITextureRegion vLineTexture;
	private final ITextureRegion hLineTexture;

	public BasicWireBlockModel(ITextureRegion crossTexture, ITextureRegion vLineTexture, ITextureRegion hLineTexture, IBlock block, int color) {
		super(block, color);

		if (!(block instanceof BasicWireBlock)) {
			throw new IllegalArgumentException(block + " is not a BasicWireBlock!");
		}

		this.crossTexture = crossTexture;
		this.vLineTexture = vLineTexture;
		this.hLineTexture = hLineTexture;
	}

	@Override
	public void tessellate(IClientWorld world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
		int color = getWireColor(state);
		float lightness = getLightness(world, pos);

		Direction connectionDir = null;
		int connectionCount = 0;

		for (Direction dir : Direction.HORIZONTAL) {
			WireSide connection = BasicWireBlock.getConnection(state, dir);

			if (connection != WireSide.NONE) {
				if (connection == WireSide.UP)
					addSideQuad(builder, pos, dir, color, lightness);

				if (connectionCount == 0)
					connectionDir = dir;

				connectionCount++;
			}
		}

		if (connectionCount == 2 && BasicWireBlock.hasConnection(state, connectionDir.getOpposite())) {
			addLineQuad(builder, pos, connectionDir.getAxis(), color, lightness);
		} else {
			addCrossQuad(builder, pos, state, color, lightness);
		}
	}

	private void addSideQuad(VertexAttribBuilder builder, IBlockPosition pos, Direction dir, int color, float lightness) {
		addAlignedQuad(builder, pos, dir.getOpposite(), 1.0f - WIRE_OFFSET, vLineTexture, color, lightness);
	}

	private void addLineQuad(VertexAttribBuilder builder, IBlockPosition pos, Axis axis, int color, float lightness) {
		ITextureRegion lineTexture = (axis == Axis.X) ? vLineTexture : hLineTexture;
		addAlignedQuad(builder, pos, Direction.UP, 1.0f - WIRE_OFFSET, lineTexture, color, lightness);
	}

	private void addCrossQuad(VertexAttribBuilder builder, IBlockPosition pos, IBlockState state, int color, float lightness) {
		float u0 = BasicWireBlock.hasConnection(state, Direction.NORTH) ? 0.0f : SIDE_CUT_OFFSET;
		float u1 = BasicWireBlock.hasConnection(state, Direction.SOUTH) ? 1.0f : (1.0f - SIDE_CUT_OFFSET);
		float v0 = BasicWireBlock.hasConnection(state, Direction.WEST)  ? 0.0f : SIDE_CUT_OFFSET;
		float v1 = BasicWireBlock.hasConnection(state, Direction.EAST)  ? 1.0f : (1.0f - SIDE_CUT_OFFSET);

		addQuad(builder, pos, Direction.UP, 1.0f - WIRE_OFFSET, crossTexture, u0, v0, u1, v1, color, lightness);
	}
}
