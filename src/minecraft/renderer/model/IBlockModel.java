package minecraft.renderer.model;

import minecraft.graphic.tessellator.VertexAttribBuilder;
import minecraft.world.World;
import minecraft.world.block.IBlockPosition;

public interface IBlockModel {

	public void tessellate(World world, IBlockPosition pos, VertexAttribBuilder builder);

}
