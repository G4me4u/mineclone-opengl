package minecraft.renderer.model;

import minecraft.graphic.opengl.texture.ITextureRegion;
import minecraft.graphic.tessellator.VertexAttribBuilder;
import minecraft.world.World;
import minecraft.world.block.IBlockPosition;

public class PlantBlockModel extends AbstractBlockModel {

	private final ITextureRegion texture;
	
	public PlantBlockModel(ITextureRegion texture) {
		this.texture = texture;
	}
	
	@Override
	public void tessellate(World world, IBlockPosition pos, VertexAttribBuilder builder) {
		float x0 = pos.getX();
		float y0 = pos.getY();
		float z0 = pos.getZ();
		
		float x1 = x0 + 1.0f;
		float y1 = y0 + 1.0f;
		float z1 = z0 + 1.0f;
	
		float lightness = getLightness(world, pos);
		
		builder.putFloat3(x0, y0, z0);
		builder.putFloat2(texture.getU0(), texture.getV0());
		builder.putFloat(lightness);
		builder.next();
		builder.putFloat3(x1, y0, z1);
		builder.putFloat2(texture.getU1(), texture.getV0());
		builder.putFloat(lightness);
		builder.next();
		builder.putFloat3(x1, y1, z1);
		builder.putFloat2(texture.getU1(), texture.getV1());
		builder.putFloat(lightness);
		builder.next();
		builder.putFloat3(x0, y1, z0);
		builder.putFloat2(texture.getU0(), texture.getV1());
		builder.putFloat(lightness);
		builder.next();

		builder.putFloat3(x1, y0, z0);
		builder.putFloat2(texture.getU0(), texture.getV0());
		builder.putFloat(lightness);
		builder.next();
		builder.putFloat3(x0, y0, z1);
		builder.putFloat2(texture.getU1(), texture.getV0());
		builder.putFloat(lightness);
		builder.next();
		builder.putFloat3(x0, y1, z1);
		builder.putFloat2(texture.getU1(), texture.getV1());
		builder.putFloat(lightness);
		builder.next();
		builder.putFloat3(x1, y1, z0);
		builder.putFloat2(texture.getU0(), texture.getV1());
		builder.putFloat(lightness);
		builder.next();
	}
}
