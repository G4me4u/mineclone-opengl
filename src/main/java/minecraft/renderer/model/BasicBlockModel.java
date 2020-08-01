package minecraft.renderer.model;

import minecraft.graphic.opengl.texture.ITextureRegion;
import minecraft.graphic.tessellator.VertexAttribBuilder;
import minecraft.world.Direction;
import minecraft.world.World;
import minecraft.world.block.IBlockPosition;

public class BasicBlockModel extends AbstractBlockModel {

	private final ITextureRegion topTexture;
	private final ITextureRegion bottomTexture;
	private final ITextureRegion sideTexture;
	
	public BasicBlockModel(ITextureRegion texture) {
		this(texture, texture, texture);
	}
	
	public BasicBlockModel(ITextureRegion top, ITextureRegion bottom, ITextureRegion side) {
		this.topTexture = top;
		this.bottomTexture = bottom;
		this.sideTexture = side;
	}
	
	@Override
	public void tessellate(World world, IBlockPosition pos, VertexAttribBuilder builder) {
		// FRONT
		addBlockFace(world, pos, builder, Direction.NORTH, sideTexture);
		// BACK
		addBlockFace(world, pos, builder, Direction.SOUTH, sideTexture);
		
		// LEFT
		addBlockFace(world, pos, builder, Direction.WEST, sideTexture);
		// RIGHT
		addBlockFace(world, pos, builder, Direction.EAST, sideTexture);

		// TOP
		addBlockFace(world, pos, builder, Direction.UP, topTexture);
		// BOTTOM
		addBlockFace(world, pos, builder, Direction.DOWN, bottomTexture);
	}
}
