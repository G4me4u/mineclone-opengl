package mineclone.client.renderer.world;

import mineclone.common.math.Mat4;
import mineclone.common.math.ViewFrustum;

public class WorldCamera {

	private float x;
	private float y;
	private float z;
	
	private float rotX;
	private float rotY;
	
	private final Mat4 viewMat;
	private final Mat4 projMat;
	private final Mat4 projViewMat;

	private final ViewFrustum viewFrustum;
	
	public WorldCamera() {
		viewMat = new Mat4();
		projMat = new Mat4();
		projViewMat = new Mat4();
		
		viewFrustum = new ViewFrustum();
	}
	
	public void setPerspective(float fov, float aspect, float near, float far) {
		projMat.toPerspective(fov, aspect, near, far);
	
		updateProjViewMat();
	}
	
	public void setView(float x, float y, float z, float rotX, float rotY) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.rotX = rotX;
		this.rotY = rotY;

		viewMat.toIdentity();
		viewMat.rotateX(rotX).rotateY(rotY);
		viewMat.translate(x, y, z);

		updateProjViewMat();
	}
	
	private void updateProjViewMat() {
		projMat.mul(viewMat, projViewMat);
		
		viewFrustum.initFrustum(projViewMat);
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
	
	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public Mat4 getProjMat() {
		return projMat;
	}
	
	public Mat4 getViewMat() {
		return viewMat;
	}
	
	public Mat4 getProjViewMat() {
		return projViewMat;
	}

	public ViewFrustum getViewFrustum() {
		return viewFrustum;
	}
}
