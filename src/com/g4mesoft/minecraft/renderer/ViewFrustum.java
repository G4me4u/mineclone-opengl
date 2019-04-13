package com.g4mesoft.minecraft.renderer;

import com.g4mesoft.math.Mat4f;
import com.g4mesoft.math.MathUtils;
import com.g4mesoft.math.Vec3f;

public class ViewFrustum {

	private final Plane[] viewPlanes;
	
	public ViewFrustum() {
		viewPlanes = new Plane[6];
		
		for (int i = 0; i < 6; i++)
			viewPlanes[i] = new Plane();
	}
	
	public void initFrustum(Mat4f pvm) {
		// LEFT RIGHT
		viewPlanes[0].set(pvm.m03 + pvm.m00, pvm.m13 + pvm.m10, pvm.m23 + pvm.m20, pvm.m33 + pvm.m30);
		viewPlanes[1].set(pvm.m03 - pvm.m00, pvm.m13 - pvm.m10, pvm.m23 - pvm.m20, pvm.m33 - pvm.m30);

		// BOTTOM TOP
		viewPlanes[2].set(pvm.m03 + pvm.m01, pvm.m13 + pvm.m11, pvm.m23 + pvm.m21, pvm.m33 + pvm.m31);
		viewPlanes[3].set(pvm.m03 - pvm.m01, pvm.m13 - pvm.m11, pvm.m23 - pvm.m21, pvm.m33 - pvm.m31);

		// NEAR FAR
		viewPlanes[4].set(pvm.m03 + pvm.m02, pvm.m13 + pvm.m12, pvm.m23 + pvm.m22, pvm.m33 + pvm.m32);
		viewPlanes[5].set(pvm.m03 - pvm.m02, pvm.m13 - pvm.m12, pvm.m23 - pvm.m22, pvm.m33 - pvm.m32);
	}
	
	public boolean pointInView(Vec3f point) {
		return pointInView(point.x, point.y, point.z);
	}

	public boolean pointInView(float x, float y, float z) {
		for (Plane plane : viewPlanes) {
			Vec3f n = plane.normal;
			
			if (n.x * x + n.y * y + n.z * z + plane.d <= 0)
				return false;
		}
		
		return true;
	}
	
	public boolean sphereInView(Vec3f center, float radius) {
		return sphereInView(center.x, center.y, center.z, radius);
	}

	public boolean sphereInView(float xc, float yc, float zc, float radius) {
		for (Plane plane : viewPlanes) {
			Vec3f n = plane.normal;
			
			if (n.x * xc + n.y * yc + n.z * zc + plane.d + radius <= 0)
				return false;
		}
		
		return true;
	}
	
	private class Plane {
		
		private final Vec3f normal;
		private float d;
		
		public Plane() {
			normal = new Vec3f();
			d = 0.0f;
		}
		
		public void set(float a, float b, float c, float d) {
			normal.set(a, b, c);
			this.d = d;
			
			float lenSqr = normal.lengthSqr();
			if (lenSqr > MathUtils.EPSILON * MathUtils.EPSILON) {
				float s = 1.0f / MathUtils.sqrt(lenSqr);
				normal.mul(s);
				this.d *= s;
			}
		}
	}
}
