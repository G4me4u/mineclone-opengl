package minecraft.client.renderer.model;

import minecraft.client.graphic.ITextureRegion;
import minecraft.client.graphic.tessellator.VertexAttribBuilder;
import minecraft.common.world.Direction;
import minecraft.common.world.World;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.BlockState;

public abstract class AbstractBlockModel implements IBlockModel {

	private static final float AMBIENT_LIGHT = 0.4f;
	
	protected void addBlockFace(World world, IBlockPosition pos, VertexAttribBuilder builder, Direction face, ITextureRegion tex) {
		IBlockPosition offsetPos = pos.getOffset(face);
		
		if (world.isLoadedBlock(offsetPos)) {
			BlockState offsetState = world.getBlockState(offsetPos);
			
			if (!offsetState.getBlock().isSolid()) {
				float lightness = getLightness(world, offsetPos);
		
				addAlignedQuad(builder, pos, face, 0.0f, tex, lightness);
			}
		}
	}

	protected void addAlignedQuad(VertexAttribBuilder builder, IBlockPosition pos, Direction face, float offset, ITextureRegion tex, float lightness) {
		float x = pos.getX();
		float y = pos.getY();
		float z = pos.getZ();
		
		switch (face) {
		case NORTH: // FRONT
			builder.putFloat3(x + 0.0f, y + 0.0f, z + 1.0f - offset);
			builder.putFloat2(tex.getU0(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f, y + 0.0f, z + 1.0f - offset);
			builder.putFloat2(tex.getU1(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f, y + 1.0f, z + 1.0f - offset);
			builder.putFloat2(tex.getU1(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 0.0f, y + 1.0f, z + 1.0f - offset);
			builder.putFloat2(tex.getU0(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();

			break;
		case SOUTH: // BACK
			builder.putFloat3(x + 0.0f, y + 0.0f, z + 0.0f + offset);
			builder.putFloat2(tex.getU1(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 0.0f, y + 1.0f, z + 0.0f + offset);
			builder.putFloat2(tex.getU1(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f, y + 1.0f, z + 0.0f + offset);
			builder.putFloat2(tex.getU0(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f, y + 0.0f, z + 0.0f + offset);
			builder.putFloat2(tex.getU0(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();

			break;
		case WEST: // LEFT
			builder.putFloat3(x + 0.0f + offset, y + 0.0f, z + 0.0f);
			builder.putFloat2(tex.getU0(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 0.0f + offset, y + 0.0f, z + 1.0f);
			builder.putFloat2(tex.getU1(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 0.0f + offset, y + 1.0f, z + 1.0f);
			builder.putFloat2(tex.getU1(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 0.0f + offset, y + 1.0f, z + 0.0f);
			builder.putFloat2(tex.getU0(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();
			
			break;
		case EAST: // RIGHT
			builder.putFloat3(x + 1.0f - offset, y + 0.0f, z + 0.0f);
			builder.putFloat2(tex.getU1(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f - offset, y + 1.0f, z + 0.0f);
			builder.putFloat2(tex.getU1(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f - offset, y + 1.0f, z + 1.0f);
			builder.putFloat2(tex.getU0(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f - offset, y + 0.0f, z + 1.0f);
			builder.putFloat2(tex.getU0(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			
			break;
		case UP: // TOP
			builder.putFloat3(x + 0.0f, y + 1.0f - offset, z + 0.0f);
			builder.putFloat2(tex.getU0(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 0.0f, y + 1.0f - offset, z + 1.0f);
			builder.putFloat2(tex.getU0(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f, y + 1.0f - offset, z + 1.0f);
			builder.putFloat2(tex.getU1(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f, y + 1.0f - offset, z + 0.0f);
			builder.putFloat2(tex.getU1(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			
			break;
		case DOWN: // BOTTOM
			builder.putFloat3(x + 0.0f, y + 0.0f + offset, z + 0.0f);
			builder.putFloat2(tex.getU0(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f, y + 0.0f + offset, z + 0.0f);
			builder.putFloat2(tex.getU1(), tex.getV0());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 1.0f, y + 0.0f + offset, z + 1.0f);
			builder.putFloat2(tex.getU1(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();
			builder.putFloat3(x + 0.0f, y + 0.0f + offset, z + 1.0f);
			builder.putFloat2(tex.getU0(), tex.getV1());
			builder.putFloat(lightness);
			builder.next();

			break;
		}
	}
	
	protected float getLightness(World world, IBlockPosition pos) {
		return (world.getHighestPoint(pos) < pos.getY()) ? 1.0f : AMBIENT_LIGHT;
	}
}
