package minecraft.client.renderer.model;

import minecraft.client.graphic.tessellator.VertexAttribBuilder;
import minecraft.common.world.IClientWorld;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.IBlockState;

public interface IBlockModel {

	public void tessellate(IClientWorld world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder);

}
