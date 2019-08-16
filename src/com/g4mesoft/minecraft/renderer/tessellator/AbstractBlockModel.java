package com.g4mesoft.minecraft.renderer.tessellator;

import com.g4mesoft.graphics3d.VertexTessellator3D;
import com.g4mesoft.minecraft.renderer.WorldRenderer;
import com.g4mesoft.minecraft.world.Direction;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.IBlockPosition;

public abstract class AbstractBlockModel implements IBlockModel {

	private static final float AMBIENT_LIGHT = 0.4f;
	
	protected void addBlockFace(World world, IBlockPosition pos, VertexTessellator3D tessellator, Direction face, TextureRegion tex) {
		if (world.getBlockState(pos.getOffset(face)).getBlock().isSolid())
			return;
		
		float x = pos.getX();
		float y = pos.getY();
		float z = pos.getZ();
		
		IBlockPosition offsetPos = pos.getOffset(face);
		float lightness = world.getHighestPoint(offsetPos) <= offsetPos.getY() ? 1.0f : AMBIENT_LIGHT;
		
		switch (face) {
		case NORTH: // FRONT
			tessellator.addVertex(x + 0.0f, y + 0.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 0.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 1.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 0.0f, y + 1.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);

			break;
		case SOUTH: // BACK
			tessellator.addVertex(x + 0.0f, y + 0.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 0.0f, y + 1.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 1.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 0.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);

			break;
		case WEST: // LEFT
			tessellator.addVertex(x + 0.0f, y + 0.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 0.0f, y + 0.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 0.0f, y + 1.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 0.0f, y + 1.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			
			break;
		case EAST: // RIGHT
			tessellator.addVertex(x + 1.0f, y + 0.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 1.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 1.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 0.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			
			break;
		case UP: // TOP
			tessellator.addVertex(x + 0.0f, y + 1.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 0.0f, y + 1.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 1.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 1.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			
			break;
		case DOWN: // BOTTOM
			tessellator.addVertex(x + 0.0f, y + 0.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 0.0f, z + 0.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v0);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 1.0f, y + 0.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u1, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);
			tessellator.addVertex(x + 0.0f, y + 0.0f, z + 1.0f);
			tessellator.setExtraVertexVec2(WorldRenderer.LOCATION_TEX_UV, tex.u0, tex.v1);
			tessellator.setExtraVertexData(WorldRenderer.LOCATION_LIGHTNESS, lightness);

			break;
		}
	}
}
