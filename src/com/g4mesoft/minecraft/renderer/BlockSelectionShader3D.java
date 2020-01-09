package com.g4mesoft.minecraft.renderer;

import com.g4mesoft.graphics3d.Fragment3D;
import com.g4mesoft.graphics3d.IShader3D;
import com.g4mesoft.graphics3d.Triangle3D;
import com.g4mesoft.graphics3d.Vertex3D;
import com.g4mesoft.math.Mat4f;
import com.g4mesoft.minecraft.world.block.IBlockPosition;

public class BlockSelectionShader3D implements IShader3D {

	private static final float SCALE = 1.005f;
	
	private static final int SELECTION_COLOR_R = 0xE0;
	private static final int SELECTION_COLOR_G = 0xE0;
	private static final int SELECTION_COLOR_B = 0xFF;
	
	private final WorldCamera camera;
	private final Mat4f projViewModlMat;
	private final Mat4f modlMat;
	
	private IBlockPosition blockPos;
	private int selectionAlpha;
	
	public BlockSelectionShader3D(WorldCamera camera) {
		this.camera = camera;
		
		projViewModlMat = new Mat4f();
		
		modlMat = new Mat4f().setScale(SCALE)
			.setTranslation((1.0f - SCALE) * 0.5f);
	
		blockPos = null;
		selectionAlpha = 0;
	}
	
	@Override
	public void prepareShader() {
		camera.getProjViewMatrix().copy(projViewModlMat);
		
		if (blockPos != null)
			projViewModlMat.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	
		projViewModlMat.mul(modlMat);
	}

	@Override
	public void projectVertices(Triangle3D result, Vertex3D v0, Vertex3D v1, Vertex3D v2) {
		projViewModlMat.mul(v0.pos, result.v0.pos);
		projViewModlMat.mul(v1.pos, result.v1.pos);
		projViewModlMat.mul(v2.pos, result.v2.pos);
	}

	@Override
	public boolean fragment(Vertex3D vert, Fragment3D fragment) {
		fragment.blend(selectionAlpha, SELECTION_COLOR_R, SELECTION_COLOR_G, SELECTION_COLOR_B);
		return true;
	}

	@Override
	public int getOutputSize() {
		return 0;
	}

	public void setSelectionPosition(IBlockPosition blockPos) {
		this.blockPos = blockPos;
	}

	public void setSelectionAlpha(int alpha) {
		selectionAlpha = alpha;
	}
}
