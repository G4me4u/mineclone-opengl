package minecraft.client.renderer.model;

import minecraft.client.graphic.tessellator.VertexAttribBuilder;
import minecraft.common.world.World;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.IBlockState;

public interface IBlockModel {

	public void tessellate(World world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder);

}
