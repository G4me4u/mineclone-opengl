package minecraft.client.renderer.model;

import minecraft.client.graphic.ITextureRegion;
import minecraft.client.graphic.tessellator.VertexAttribBuilder;
import minecraft.common.world.World;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.IBlockState;

public class PlantBlockModel extends AbstractBlockModel {

	private final ITextureRegion texture;
	
	public PlantBlockModel(ITextureRegion texture) {
		this.texture = texture;
	}
	
	@Override
	public void tessellate(World world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
		float x0 = pos.getX();
		float y0 = pos.getY();
		float z0 = pos.getZ();
		
		float x1 = x0 + 1.0f;
		float y1 = y0 + 1.0f;
		float z1 = z0 + 1.0f;

		byte r = (byte)0xFF;
		byte g = (byte)0xFF;
		byte b = (byte)0xFF;
		byte a = (byte)0xFF;
		
		float lightness = getLightness(world, pos);
		
		putVert(builder, x0, y0, z0, texture.getU0(), texture.getV0(), r, g, b, a, lightness);
		putVert(builder, x1, y0, z1, texture.getU1(), texture.getV0(), r, g, b, a, lightness);
		putVert(builder, x1, y1, z1, texture.getU1(), texture.getV1(), r, g, b, a, lightness);
		putVert(builder, x0, y1, z0, texture.getU0(), texture.getV1(), r, g, b, a, lightness);
		
		putVert(builder, x1, y0, z1, texture.getU0(), texture.getV0(), r, g, b, a, lightness);
		putVert(builder, x0, y0, z0, texture.getU1(), texture.getV0(), r, g, b, a, lightness);
		putVert(builder, x0, y1, z0, texture.getU1(), texture.getV1(), r, g, b, a, lightness);
		putVert(builder, x1, y1, z1, texture.getU0(), texture.getV1(), r, g, b, a, lightness);

		putVert(builder, x1, y0, z0, texture.getU0(), texture.getV0(), r, g, b, a, lightness);
		putVert(builder, x0, y0, z1, texture.getU1(), texture.getV0(), r, g, b, a, lightness);
		putVert(builder, x0, y1, z1, texture.getU1(), texture.getV1(), r, g, b, a, lightness);
		putVert(builder, x1, y1, z0, texture.getU0(), texture.getV1(), r, g, b, a, lightness);
		
		putVert(builder, x0, y0, z1, texture.getU0(), texture.getV0(), r, g, b, a, lightness);
		putVert(builder, x1, y0, z0, texture.getU1(), texture.getV0(), r, g, b, a, lightness);
		putVert(builder, x1, y1, z0, texture.getU1(), texture.getV1(), r, g, b, a, lightness);
		putVert(builder, x0, y1, z1, texture.getU0(), texture.getV1(), r, g, b, a, lightness);
	}
}
