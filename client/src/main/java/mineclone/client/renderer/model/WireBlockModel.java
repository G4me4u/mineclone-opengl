package mineclone.client.renderer.model;

import static mineclone.common.world.block.WireBlock.CONNECTIONS;
import static mineclone.common.world.block.WireBlock.EAST_CONNECTION;
import static mineclone.common.world.block.WireBlock.NORTH_CONNECTION;
import static mineclone.common.world.block.WireBlock.SOUTH_CONNECTION;
import static mineclone.common.world.block.WireBlock.WEST_CONNECTION;

import mineclone.client.graphic.ITextureRegion;
import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.common.util.ColorUtil;
import mineclone.common.world.Axis;
import mineclone.common.world.Direction;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.WireConnection;
import mineclone.common.world.block.signal.wire.Wire;
import mineclone.common.world.block.signal.wire.WireType;
import mineclone.common.world.block.state.IBlockState;

public class WireBlockModel extends AbstractBlockModel {

	private static final float WIRE_OFFSET = 0.005f;
	private static final float SIDE_CUT_OFFSET = 5.0f / 16.0f;
	
	private final ITextureRegion crossTexture;
	private final ITextureRegion vLineTexture;
	private final ITextureRegion hLineTexture;

	private final Wire wire;
	private final WireType type;

	private final int[] colorTable;
	
	public WireBlockModel(ITextureRegion crossTexture, ITextureRegion vLineTexture, ITextureRegion hLineTexture, Wire wire, int color) {
		this.crossTexture = crossTexture;
		this.vLineTexture = vLineTexture;
		this.hLineTexture = hLineTexture;
	
		this.wire = wire;
		this.type = this.wire.getWireType();

		this.colorTable = createColorTable(color);
	}
	
	private int[] createColorTable(int baseColor) {
		int br = ColorUtil.unpackR(baseColor);
		int bg = ColorUtil.unpackG(baseColor);
		int bb = ColorUtil.unpackB(baseColor);

		ColorUtil.checkARGB(0, br, bg, bb);

		int min = type.min();
		int max = type.max();
		int count = (max - min) + 1;

		int[] table = new int[count];
		int index = 0;

		for (int signal = min; signal <= max; signal++) {
			float f = (float)(signal - min) / (max - min);

			float r = ColorUtil.normalize(br) * ((signal == min) ? 0.3F : (0.6F * f + 0.4F));
			float g = ColorUtil.normalize(bg) * ((signal == min) ? 0.3F : (0.6F * f + 0.4F));
			float b = ColorUtil.normalize(bb) * ((signal == min) ? 0.3F : (0.6F * f + 0.4F));

			table[index++] = ColorUtil.pack(r, g, b);
		}

		return table;
	}

	private int getWireColor(IClientWorld world, IBlockPosition pos, IBlockState state) {
		return colorTable[wire.getSignal(state) - type.min()];
	}

	@Override
	public void tessellate(IClientWorld world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
		int color = getWireColor(world, pos, state);
		float lightness = getLightness(world, pos);
		
		Direction connectionDir = null;
		int connectionCount = 0;
		
		for (Direction dir : Direction.HORIZONTAL) {
			WireConnection connection = state.get(CONNECTIONS.get(dir));
			
			if (connection != WireConnection.NONE) {
				if (connection == WireConnection.UP)
					addSideQuad(builder, pos, dir, color, lightness);
				
				if (connectionCount == 0)
					connectionDir = dir;
				
				connectionCount++;
			}
		}
		
		if (connectionCount == 2 && state.get(CONNECTIONS.get(connectionDir.getOpposite())) != WireConnection.NONE) {
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
		float u0 = (state.get(NORTH_CONNECTION) != WireConnection.NONE) ? 0.0f : SIDE_CUT_OFFSET;
		float u1 = (state.get(SOUTH_CONNECTION) != WireConnection.NONE) ? 1.0f : (1.0f - SIDE_CUT_OFFSET);
		float v0 = (state.get(WEST_CONNECTION)  != WireConnection.NONE) ? 0.0f : SIDE_CUT_OFFSET;
		float v1 = (state.get(EAST_CONNECTION)  != WireConnection.NONE) ? 1.0f : (1.0f - SIDE_CUT_OFFSET);

		addQuad(builder, pos, Direction.UP, 1.0f - WIRE_OFFSET, crossTexture, u0, v0, u1, v1, color, lightness);
	}
}
