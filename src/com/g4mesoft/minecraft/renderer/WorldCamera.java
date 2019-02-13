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

	private boolean viewMatrixNeedsRefresh;
	private boolean finalMatrixNeedsRefresh;
	
	public WorldCamera() {
		viewMatrix = new Mat4f();
		projMatrix = new Mat4f();
		projViewMatrix = new Mat4f();
		
		finalMatrixNeedsRefresh = false;
	}
	
	public void setPerspective(float fov, float aspect, float near, float far) {
		projMatrix.toPerspective(fov, aspect, near, far);
		finalMatrixNeedsRefresh = true;
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
		
		viewMatrixNeedsRefresh = true;
		finalMatrixNeedsRefresh = true;
	}

	public void setRotation(float rx, float ry) {
		this.rx = rx;
		this.ry = ry;
		
		viewMatrixNeedsRefresh = true;
		finalMatrixNeedsRefresh = true;
	}
}
