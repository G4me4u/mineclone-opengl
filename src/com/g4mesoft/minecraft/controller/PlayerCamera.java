package com.g4mesoft.minecraft.controller;

import com.g4mesoft.input.mouse.MouseInputListener;

public class PlayerCamera {

	public float yaw;
	public float pitch;
	
	private float sensitivity;
	
	private boolean rotationChanged;
	
	public PlayerCamera(float sensitivity) {
		this.sensitivity = sensitivity;
	}
	
	public void update() {
		MouseInputListener mouse = MouseInputListener.getInstance();
		
		if (mouse.isGrabActive()) {
			int dx = mouse.getDeltaX();
			int dy = mouse.getDeltaY();
			if (dx != 0 || dy != 0) {
				yaw -= dy * sensitivity;
				pitch -= dx * sensitivity;
				
				if (yaw > 90.0f) {
					yaw = 90.0f;
				} else if (yaw < -90.0f) {
					yaw = -90.0f;
				}
				
				rotationChanged = true;
			}
		}
	}
	
	public void setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
	}
	
	public boolean hasRotationChanged() {
		return rotationChanged;
	}
}
