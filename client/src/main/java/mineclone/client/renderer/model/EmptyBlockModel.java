package mineclone.client.renderer.model;

import mineclone.client.graphic.tessellator.VertexAttribBuilder;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class EmptyBlockModel implements IBlockModel {

	public static final IBlockModel INSTANCE = new EmptyBlockModel();
	
	private EmptyBlockModel() {
	}
	
	@Override
	public void tessellate(IClientWorld world, IBlockPosition pos, IBlockState state, VertexAttribBuilder builder) {
	}
}
