package minecraft.client.renderer.model;

import minecraft.client.graphic.ITextureRegion;
import minecraft.client.graphic.tessellator.VertexAttribBuilder;
import minecraft.common.util.ColorUtil;
import minecraft.common.world.Axis;
import minecraft.common.world.Direction;
import minecraft.common.world.World;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.WireConnection;
import minecraft.common.world.block.state.IBlockState;

import static minecraft.common.world.block.RedstoneWireBlock.*;

public class WireBlockModel extends AbstractBlockModel {

	private static final float WIRE_OFFSET = 0.0001f;
	private static final float SIDE_CUT_OFFSET = 5.0f / 16.0f;
	
	private final ITextureRegion crossTexture;
	private final ITextureRegion vLineTexture;
	private final ITextureRegion hLineTexture;

	private final int[] wireColorTable;
	
	public WireBlockModel(ITextureRegion crossTexture, ITextureRegion vLineTexture, ITextureRegion hLineTexture) {
		this.crossTexture = crossTexture;
		this.vLineTexture = vLineTexture;
		this.hLineTexture = hLineTexture;
	
		wireColorTable = createColorTable();
	}
	
	private static int[] createColorTable() {
		int count = POWER.getValueCount();
		int maxPower = POWER.getValue(count - 1);
		
		int[] colorTable = new int[count];
		for (int i = 0; i < count; i++)
			colorTable[i] = createWireColor(POWER.getValue(i), maxPower);
	
		return colorTable;
	}
	
	private static int createWireColor(int power, int maxPower) {
		float level = (float)power / maxPower;

		float r = (power == 0) ? 0.3f : (level * 0.6f + 0.4f);
		float g = level * level * 0.7f - 0.5f;
		float b = level * level * 0.6f - 0.7f;
		
		return ColorUtil.pack(r, g, b);
	}
	
	private int getWireColor(World world, IBlockPosition pos, IBlockState state) {
		return wireColorTable[POWER.getValueIndex(state.get(POWER))];
	}

	@Override
	public void tessellate(World world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
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
