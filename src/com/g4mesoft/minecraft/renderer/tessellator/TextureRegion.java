package com.g4mesoft.minecraft.renderer.tessellator;

import com.g4mesoft.graphics3d.Texture3D;

public class TextureRegion {

	public final float u0;
	public final float v0;
	public final float u1;
	public final float v1;
	
	public TextureRegion(float u0, float v0, float u1, float v1) {
		this.u0 = u0;
		this.v0 = v0;
		this.u1 = u1;
		this.v1 = v1;
	}
	
	public int sampleTexture(Texture3D tex, float u, float v) {
		u = u0 + (u1 - u0) * u;
		v = v0 + (v1 - v0) * v;
		
		return tex.samplePixel(u, v);
	}

	public TextureRegion getSubRegion(float u0, float v0, float u1, float v1) {
		u0 = this.u0 + (this.u1 - this.u0) * u0;
		v0 = this.v0 + (this.v1 - this.v0) * v0;
		
		u1 = this.u0 + (this.u1 - this.u0) * u1;
		v1 = this.v0 + (this.v1 - this.v0) * v1;
	
		return new TextureRegion(u0, v0, u1, v1);
	}
	
	public boolean contains(TextureRegion other) {
		if (other.u1 < u0 || other.u0 > u1)
			return false;
		if (other.v1 < v0 || other.v0 > v1)
			return false;
		return true;
	}
	
	public boolean contains(float u, float v) {
		return !(u < u0 || u > u1 || v < v0 || v > v1);
	}
}
