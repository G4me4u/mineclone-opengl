package com.g4mesoft.minecraft.renderer.tessellator;

import com.g4mesoft.graphics3d.VertexTessellator3D;
import com.g4mesoft.minecraft.renderer.WorldRenderer;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.IBlockPosition;

public class PlantBlockModel extends AbstractBlockModel {

	private final TextureRegion texture;
	
	public PlantBlockModel(TextureRegion texture) {
		this.texture = texture;
	}
	
	@Override
	public void tessellateBlock(World world, IBlockPosition pos, VertexTessellator3D tessellator) {
		float x0 = pos.getX();
		float y0 = pos.getY();
		float z0 = pos.getZ();
		
		float x1 = x0 + 1.0f;
		float y1 = y0 + 1.0f;
		float z1 = z0 + 1.0f;
	
		float lightness = getLightness(world, pos);
		
		tessellator.addVertex(x0, y0, z0);
		tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, texture.u0, texture.v0);
		tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
		tessellator.addVertex(x1, y0, z1);
		tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, texture.u1, texture.v0);
		tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
		tessellator.addVertex(x1, y1, z1);
		tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, texture.u1, texture.v1);
		tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
		tessellator.addVertex(x0, y1, z0);
		tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, texture.u0, texture.v1);
		tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);

		tessellator.addVertex(x1, y0, z0);
		tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, texture.u0, texture.v0);
		tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
		tessellator.addVertex(x0, y0, z1);
		tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, texture.u1, texture.v0);
		tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
		tessellator.addVertex(x0, y1, z1);
		tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, texture.u1, texture.v1);
		tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
		tessellator.addVertex(x1, y1, z0);
		tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, texture.u0, texture.v1);
		tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
	}
}
