package com.g4mesoft.minecraft.renderer;

import com.g4mesoft.graphics3d.IShader3D;
import com.g4mesoft.graphics3d.Triangle3D;
import com.g4mesoft.graphics3d.Vertex3D;
import com.g4mesoft.math.Mat4f;

public class WorldShader3D implements IShader3D {

	private final Mat4f projMat;
	public final Mat4f viewMat;
	
	private final Mat4f projViewMat;
	
	public WorldShader3D() {
		projMat = new Mat4f();
		viewMat = new Mat4f();
	
		projViewMat = new Mat4f();
	}
	
	public void setPerspective(float fov, float aspect, float near, float far) {
		projMat.toPerspective(fov, aspect, near, far);
	}
	
	@Override
	public void prepareShader() {
		projMat.copy(projViewMat);
		projViewMat.mul(viewMat);
	}

	@Override
	public void projectVertices(Triangle3D result, Vertex3D v0, Vertex3D v1, Vertex3D v2) {
		projViewMat.mul(v0.pos, result.v0.pos);
		projViewMat.mul(v1.pos, result.v1.pos);
		projViewMat.mul(v2.pos, result.v2.pos);
		
		result.v0.storeFloat(0, v0.loadFloat(0));
		result.v0.storeFloat(1, v0.loadFloat(1));
		result.v0.storeFloat(2, v0.loadFloat(2));

		result.v1.storeFloat(0, v1.loadFloat(0));
		result.v1.storeFloat(1, v1.loadFloat(1));
		result.v1.storeFloat(2, v1.loadFloat(2));

		result.v2.storeFloat(0, v2.loadFloat(0));
		result.v2.storeFloat(1, v2.loadFloat(1));
		result.v2.storeFloat(2, v2.loadFloat(2));
	}

	@Override
	public int fragment(Vertex3D vert) {
		int r = (int)(255.0f * vert.data[0]);
		int g = (int)(255.0f * vert.data[1]);
		int b = (int)(255.0f * vert.data[2]);
		return (r << 16) | (g << 8) | b;
	}

	@Override
	public int getOutputSize() {
		return 3;
	}
}
