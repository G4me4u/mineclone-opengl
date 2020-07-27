package com.g4mesoft.minecraft.renderer.tessellator;

import com.g4mesoft.graphics3d.VertexTessellator3D;
import com.g4mesoft.minecraft.world.Direction;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.IBlockPosition;

public class CropBlockModel extends AbstractBlockModel {

	private static final float PLANT_QUAD_OFFSET = 3.0f / 16.0f;
	
	private final TextureRegion texture;
	
	public CropBlockModel(TextureRegion texture) {
		this.texture = texture;
	}
	
	@Override
	public void tessellateBlock(World world, IBlockPosition pos, VertexTessellator3D tessellator) {
		float lightness = getLightness(world, pos);
		
		addAlignedQuad(tessellator, pos, Direction.NORTH, PLANT_QUAD_OFFSET, texture, lightness);
		addAlignedQuad(tessellator, pos, Direction.SOUTH, PLANT_QUAD_OFFSET, texture, lightness);
		addAlignedQuad(tessellator, pos, Direction.WEST , PLANT_QUAD_OFFSET, texture, lightness);
		addAlignedQuad(tessellator, pos, Direction.EAST , PLANT_QUAD_OFFSET, texture, lightness);
	}
}
