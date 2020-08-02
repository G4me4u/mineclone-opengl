package minecraft.client.renderer.model;

import minecraft.client.graphic.tessellator.VertexAttribBuilder;
import minecraft.common.world.World;
import minecraft.common.world.block.IBlockPosition;

public interface IBlockModel {

	public void tessellate(World world, IBlockPosition pos, VertexAttribBuilder builder);

}
