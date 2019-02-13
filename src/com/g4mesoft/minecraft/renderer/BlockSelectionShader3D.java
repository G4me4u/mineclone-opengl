package com.g4mesoft.minecraft.renderer;

import com.g4mesoft.graphics3d.IShader3D;
import com.g4mesoft.graphics3d.Triangle3D;
import com.g4mesoft.graphics3d.Vertex3D;
import com.g4mesoft.math.Mat4f;
import com.g4mesoft.minecraft.world.block.IBlockPosition;

public class BlockSelectionShader3D implements IShader3D {

	private final WorldCamera camera;
	private final Mat4f projViewMat;
	
	private IBlockPosition blockPos;
	
	public BlockSelectionShader3D(WorldCamera camera) {
		this.camera = camera;
		
		projViewMat = new Mat4f();
	}
	
	@Override
	public void prepareShader() {
		camera.getProjViewMatrix().copy(projViewMat);
		
		if (blockPos != null)
			projViewMat.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	@Override
	public void projectVertices(Triangle3D result, Vertex3D v0, Vertex3D v1, Vertex3D v2) {
		projViewMat.mul(v0.pos, result.v0.pos);
		projViewMat.mul(v1.pos, result.v1.pos);
		projViewMat.mul(v2.pos, result.v2.pos);
	}

	@Override
	public int fragment(Vertex3D vert) {
		return 0xFFFFFF;
	}

	@Override
	public int getOutputSize() {
		return 0;
	}

	public void setSelectionPosition(IBlockPosition blockPos) {
		this.blockPos = blockPos;
	}
}
