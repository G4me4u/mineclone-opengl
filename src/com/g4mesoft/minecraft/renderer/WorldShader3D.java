package com.g4mesoft.minecraft.renderer;

import com.g4mesoft.graphics3d.Fragment3D;
import com.g4mesoft.graphics3d.IShader3D;
import com.g4mesoft.graphics3d.Texture3D;
import com.g4mesoft.graphics3d.Triangle3D;
import com.g4mesoft.graphics3d.Vertex3D;
import com.g4mesoft.math.Mat4f;
import com.g4mesoft.math.MathUtils;

public class WorldShader3D implements IShader3D {

	private static final int VERTEX_OUT_SIZE = 4;
	
	private static final int FRAG_LOCATION_TEX_U = 0;
	private static final int FRAG_LOCATION_TEX_V = 1;
	private static final int FRAG_LOCATION_LIGHTNESS = 2;
	private static final int FRAG_LOCATION_DEPTH = 3;
	
	private final WorldCamera camera;
	private final Mat4f projViewMat;
	
	private final Texture3D texture;
	
	public WorldShader3D(WorldCamera camera, Texture3D texture) {
		this.camera = camera;
		this.texture = texture;
		
		projViewMat = new Mat4f();
	}
	
	@Override
	public void prepareShader() {
		camera.getProjViewMatrix().copy(projViewMat);
	}

	@Override
	public void projectVertices(Triangle3D result, Vertex3D v0, Vertex3D v1, Vertex3D v2) {
		project(result.v0, v0);
		project(result.v1, v1);
		project(result.v2, v2);
	}
	
	private void project(Vertex3D out, Vertex3D in) {
		projViewMat.mul(in.pos, out.pos);

		out.storeFloat(FRAG_LOCATION_TEX_U, in.loadFloat(WorldRenderer.LOCATION_TEX_U));
		out.storeFloat(FRAG_LOCATION_TEX_V, in.loadFloat(WorldRenderer.LOCATION_TEX_V));
		out.storeFloat(FRAG_LOCATION_LIGHTNESS, in.loadFloat(WorldRenderer.LOCATION_LIGHTNESS));
		out.storeFloat(FRAG_LOCATION_DEPTH, out.pos.z);
	}

	@Override
	public boolean fragment(Vertex3D vert, Fragment3D fragment) {
		float u = vert.loadFloat(FRAG_LOCATION_TEX_U);
		float v = vert.loadFloat(FRAG_LOCATION_TEX_V);
		
		int rgba = texture.samplePixel(u, v);
		
		if ((rgba >>> 24) < 0x80)
			return false;

		float lightness = vert.loadFloat(FRAG_LOCATION_LIGHTNESS);
		
		float r = ((rgba >>> 16) & 0xFF) * lightness;
		float g = ((rgba >>>  8) & 0xFF) * lightness;
		float b = ((rgba >>>  0) & 0xFF) * lightness;

		float fd = vert.loadFloat(FRAG_LOCATION_DEPTH) * 0.01f;
		float fog = 1.0f / MathUtils.exp(fd * fd * fd * fd * fd);
		r = r * fog + (1.0f - fog) * 0xFF;
		g = g * fog + (1.0f - fog) * 0xFF;
		b = b * fog + (1.0f - fog) * 0xFF;
		
		fragment.setRGB((int)r, (int)g, (int)b);
		return true;
	}

	@Override
	public int getOutputSize() {
		return VERTEX_OUT_SIZE;
	}
}
