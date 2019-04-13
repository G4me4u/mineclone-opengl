package com.g4mesoft.minecraft.renderer;

import com.g4mesoft.math.Mat4f;

public class WorldCamera {

	private float x;
	private float y;
	private float z;
	
	private float rx;
	private float ry;
	
	private final Mat4f viewMatrix;
	private final Mat4f projMatrix;
	private final Mat4f projViewMatrix;

	private final ViewFrustum viewFrustum;
	
	private boolean viewMatrixNeedsRefresh;
	private boolean finalMatrixNeedsRefresh;
	private boolean viewFrustumNeedsRefresh;
	
	public WorldCamera() {
		viewMatrix = new Mat4f();
		projMatrix = new Mat4f();
		projViewMatrix = new Mat4f();
		
		viewFrustum = new ViewFrustum();
		
		viewMatrixNeedsRefresh = false;
		finalMatrixNeedsRefresh = false;
		viewFrustumNeedsRefresh = false;
	}
	
	public void setPerspective(float fov, float aspect, float near, float far) {
		projMatrix.toPerspective(fov, aspect, near, far);

		invalidateView();
	}
	
	public synchronized Mat4f getViewMatrix() {
		if (viewMatrixNeedsRefresh) {
			viewMatrix.toIdentity();
			viewMatrix.rotateX(rx).rotateY(ry);
			viewMatrix.translate(x, y, z);
			
			viewMatrixNeedsRefresh = false;
		}
		
		return viewMatrix;
	}
	
	public synchronized Mat4f getProjViewMatrix() {
		if (finalMatrixNeedsRefresh) {
			projMatrix.mul(getViewMatrix(), projViewMatrix);
			
			finalMatrixNeedsRefresh = false;
		}
		
		return projViewMatrix;
	}
	
	public Mat4f getProjectionMatrix() {
		return projMatrix;
	}
	
	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		invalidateView();
	}

	public void setRotation(float rx, float ry) {
		this.rx = rx;
		this.ry = ry;
		
		invalidateView();
	}
	
	private void invalidateView() {
		viewMatrixNeedsRefresh = true;
		finalMatrixNeedsRefresh = true;
		viewFrustumNeedsRefresh = true;
	}
	
	public ViewFrustum getViewFrustum() {
		if (viewFrustumNeedsRefresh) {
			viewFrustum.initFrustum(getProjViewMatrix());
			viewFrustumNeedsRefresh = false;
		}
		
		return viewFrustum;
	}
}
