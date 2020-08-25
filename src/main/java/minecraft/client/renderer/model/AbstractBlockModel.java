package minecraft.client.renderer.model;

import minecraft.client.graphic.ITextureRegion;
import minecraft.client.graphic.tessellator.VertexAttribBuilder;
import minecraft.common.util.ColorUtil;
import minecraft.common.world.Direction;
import minecraft.common.world.IClientWorld;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.IBlockState;

public abstract class AbstractBlockModel implements IBlockModel {

	private static final float AMBIENT_LIGHT = 0.4f;

	protected void addBlockFace(IClientWorld world, IBlockPosition pos, VertexAttribBuilder builder, Direction face, ITextureRegion tex) {
		addBlockFace(world, pos, builder, face, tex, 0.0f, 0.0f, 1.0f, 1.0f, ColorUtil.WHITE);
	}

	protected void addBlockFace(IClientWorld world, IBlockPosition pos, VertexAttribBuilder builder, Direction face, ITextureRegion tex, int color) {
		addBlockFace(world, pos, builder, face, tex, 0.0f, 0.0f, 1.0f, 1.0f, color);
	}
	
	protected void addBlockFace(IClientWorld world, IBlockPosition pos, VertexAttribBuilder builder, Direction face, ITextureRegion tex, float u0, float v0, float u1, float v1) {
		addBlockFace(world, pos, builder, face, tex, u0, v0, u1, v1, ColorUtil.WHITE);
	}

	protected void addBlockFace(IClientWorld world, IBlockPosition pos, VertexAttribBuilder builder, Direction face, ITextureRegion tex, float u0, float v0, float u1, float v1, int color) {
		IBlockPosition offsetPos = pos.offset(face);
		
		if (world.isLoadedBlock(offsetPos)) {
			IBlockState offsetState = world.getBlockState(offsetPos);
			
			if (!offsetState.getBlock().isSolid()) {
				float lightness = getLightness(world, offsetPos);
		
				addQuad(builder, pos, face, 0.0f, tex, u0, v0, u1, v1, color, lightness);
			}
		}
	}

	protected void addAlignedQuad(VertexAttribBuilder builder, IBlockPosition pos, Direction face, float offset, ITextureRegion tex, float lightness) {
		addAlignedQuad(builder, pos, face, offset, tex, ColorUtil.WHITE, lightness);
	}

	protected void addAlignedQuad(VertexAttribBuilder builder, IBlockPosition pos, Direction face, float offset, ITextureRegion tex, int color, float lightness) {
		addQuad(builder, pos, face, offset, tex, 0.0f, 0.0f, 1.0f, 1.0f, color, lightness);
	}
	
	protected void addQuad(VertexAttribBuilder builder, IBlockPosition pos, Direction face, float offset, ITextureRegion tex, float u0, float v0, float u1, float v1, int color, float lightness) {
		float x = pos.getX();
		float y = pos.getY();
		float z = pos.getZ();
		
		byte a = (byte)ColorUtil.unpackA(color);
		byte r = (byte)ColorUtil.unpackR(color);
		byte g = (byte)ColorUtil.unpackG(color);
		byte b = (byte)ColorUtil.unpackB(color);
		
		// Interpolate texture
		float du = tex.getU1() - tex.getU0();
		float dv = tex.getV1() - tex.getV0();
		
		float iu0 = tex.getU0() + du * u0;
		float iv0 = tex.getV0() + dv * v0;
		float iu1 = tex.getU0() + du * u1;
		float iv1 = tex.getV0() + dv * v1;
		
		switch (face) {
		case NORTH: // BACK
			putVert(builder, x + u0, y + v0, z + offset, iu1, iv0, r, g, b, a, lightness);
			putVert(builder, x + u0, y + v1, z + offset, iu1, iv1, r, g, b, a, lightness);
			putVert(builder, x + u1, y + v1, z + offset, iu0, iv1, r, g, b, a, lightness);
			putVert(builder, x + u1, y + v0, z + offset, iu0, iv0, r, g, b, a, lightness);
			break;
		case SOUTH: // FRONT
			offset = 1.0f - offset;
			
			putVert(builder, x + u0, y + v0, z + offset, iu0, iv0, r, g, b, a, lightness);
			putVert(builder, x + u1, y + v0, z + offset, iu1, iv0, r, g, b, a, lightness);
			putVert(builder, x + u1, y + v1, z + offset, iu1, iv1, r, g, b, a, lightness);
			putVert(builder, x + u0, y + v1, z + offset, iu0, iv1, r, g, b, a, lightness);
			break;
		case WEST: // LEFT
			putVert(builder, x + offset, y + v0, z + u0, iu0, iv0, r, g, b, a, lightness);
			putVert(builder, x + offset, y + v0, z + u1, iu1, iv0, r, g, b, a, lightness);
			putVert(builder, x + offset, y + v1, z + u1, iu1, iv1, r, g, b, a, lightness);
			putVert(builder, x + offset, y + v1, z + u0, iu0, iv1, r, g, b, a, lightness);
			break;
		case EAST: // RIGHT
			offset = 1.0f - offset;
			
			putVert(builder, x + offset, y + v0, z + u0, iu1, iv0, r, g, b, a, lightness);
			putVert(builder, x + offset, y + v1, z + u0, iu1, iv1, r, g, b, a, lightness);
			putVert(builder, x + offset, y + v1, z + u1, iu0, iv1, r, g, b, a, lightness);
			putVert(builder, x + offset, y + v0, z + u1, iu0, iv0, r, g, b, a, lightness);
			break;
		case DOWN: // BOTTOM
			putVert(builder, x + v0, y + offset, z + u0, iu0, iv0, r, g, b, a, lightness);
			putVert(builder, x + v1, y + offset, z + u0, iu0, iv1, r, g, b, a, lightness);
			putVert(builder, x + v1, y + offset, z + u1, iu1, iv1, r, g, b, a, lightness);
			putVert(builder, x + v0, y + offset, z + u1, iu1, iv0, r, g, b, a, lightness);
			break;
		case UP: // TOP
			offset = 1.0f - offset;
			
			putVert(builder, x + v0, y + offset, z + u0, iu0, iv0, r, g, b, a, lightness);
			putVert(builder, x + v0, y + offset, z + u1, iu1, iv0, r, g, b, a, lightness);
			putVert(builder, x + v1, y + offset, z + u1, iu1, iv1, r, g, b, a, lightness);
			putVert(builder, x + v1, y + offset, z + u0, iu0, iv1, r, g, b, a, lightness);
			break;
		default:
			throw new IllegalStateException("Unknown face: " + face);
		}
	}
	
	protected void putVert(VertexAttribBuilder builder, float x, float y, float z, float u, float v, byte r, byte g, byte b, byte a, float lightness) {
		builder.putFloat3(x, y, z);
		builder.putFloat2(u, v);
		builder.putUByte4(r, g, b, a);
		builder.putFloat(lightness);
		builder.next();
	}
	
	protected float getLightness(IClientWorld world, IBlockPosition pos) {
		return (world.getHighestPoint(pos) < pos.getY()) ? 1.0f : AMBIENT_LIGHT;
	}
}
