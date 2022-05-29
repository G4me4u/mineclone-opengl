package mineclone.client.renderer.model;

import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public interface IBlockModel {

	public void tessellate(IClientWorld world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder);

}
