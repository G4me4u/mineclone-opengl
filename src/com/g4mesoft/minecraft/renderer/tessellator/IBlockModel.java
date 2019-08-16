package com.g4mesoft.minecraft.renderer.tessellator;

import com.g4mesoft.graphics3d.VertexTessellator3D;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.IBlockPosition;

public interface IBlockModel {

	public void tessellateBlock(World world, IBlockPosition pos, VertexTessellator3D tessellator);

}
