package com.g4mesoft.minecraft.renderer.tessellator;

import com.g4mesoft.graphics3d.VertexTessellator3D;
import com.g4mesoft.minecraft.world.Direction;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.IBlockPosition;

public class BasicBlockModel extends AbstractBlockModel {

	private final TextureRegion topTexture;
	private final TextureRegion bottomTexture;
	private final TextureRegion sideTexture;
	
	public BasicBlockModel(TextureRegion texture) {
		this(texture, texture, texture);
	}
	
	public BasicBlockModel(TextureRegion top, TextureRegion bottom, TextureRegion side) {
		this.topTexture = top;
		this.bottomTexture = bottom;
		this.sideTexture = side;
	}
	
	@Override
	public void tessellateBlock(World world, IBlockPosition pos, VertexTessellator3D tessellator) {
		// FRONT
		addBlockFace(world, pos, tessellator, Direction.NORTH, sideTexture);
		// BACK
		addBlockFace(world, pos, tessellator, Direction.SOUTH, sideTexture);
		
		// LEFT
		addBlockFace(world, pos, tessellator, Direction.WEST, sideTexture);
		// RIGHT
		addBlockFace(world, pos, tessellator, Direction.EAST, sideTexture);

		// TOP
		addBlockFace(world, pos, tessellator, Direction.UP, topTexture);
		// BOTTOM
		addBlockFace(world, pos, tessellator, Direction.DOWN, bottomTexture);
	}
}
