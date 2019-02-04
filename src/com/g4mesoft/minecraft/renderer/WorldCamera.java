package com.g4mesoft.minecraft.renderer;

import com.g4mesoft.math.Mat4f;

public class WorldCamera {

	private float x;
	private float y;
	private float z;
	
	private float rx;
	private float ry;
	
	private final Mat4f viewMatrix;
	private boolean matrixNeedsRefresh;
	
	public WorldCamera() {
		viewMatrix = new Mat4f();
		matrixNeedsRefresh = false;
	}
	
	public synchronized Mat4f getViewMatrix() {
		if (matrixNeedsRefresh) {
			viewMatrix.toIdentity();
			viewMatrix.rotateX(rx).rotateY(ry);
			viewMatrix.translate(x, y, z);
			
			matrixNeedsRefresh = false;
		}
		
		return viewMatrix;
	}
	
	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		matrixNeedsRefresh = true;
	}

	public void setRotation(float rx, float ry) {
		this.rx = rx;
		this.ry = ry;
		
		matrixNeedsRefresh = true;
	}
}
