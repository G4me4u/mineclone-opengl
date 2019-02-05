package com.g4mesoft.minecraft.controller;

import java.awt.event.KeyEvent;

import com.g4mesoft.input.key.KeyInput;
import com.g4mesoft.input.key.KeyInputListener;
import com.g4mesoft.input.key.KeySingleInput;
import com.g4mesoft.math.Mat3f;
import com.g4mesoft.math.MathUtils;
import com.g4mesoft.math.Vec3f;
import com.g4mesoft.minecraft.world.entity.PlayerEntity;

public class PlayerController {

	private final Vec3f moveVec;
	private final Mat3f rotMat;

	private boolean moved;
	private boolean sneaking;

	private final KeyInput forward;
	private final KeyInput backward;
	private final KeyInput left;
	private final KeyInput right;

	private final KeyInput jump;
	private final KeyInput sneak;
	
	public PlayerController() {
		moveVec = new Vec3f();
		rotMat = new Mat3f();
	
		moved = false;
		
		forward = new KeySingleInput("forward", KeyEvent.VK_W);
		backward = new KeySingleInput("backward", KeyEvent.VK_S);
		left = new KeySingleInput("left", KeyEvent.VK_A);
		right = new KeySingleInput("right", KeyEvent.VK_D);

		jump = new KeySingleInput("jump", KeyEvent.VK_SPACE);
		sneak = new KeySingleInput("sneak", KeyEvent.VK_SHIFT);

		KeyInputListener.getInstance().addKey(forward);
		KeyInputListener.getInstance().addKey(backward);
		KeyInputListener.getInstance().addKey(left);
		KeyInputListener.getInstance().addKey(right);

		KeyInputListener.getInstance().addKey(jump);
		KeyInputListener.getInstance().addKey(sneak);
	}
	
	public void update(PlayerEntity player) {
		moveVec.set(0.0f);
		if (forward.isPressed())
			moveVec.z--;
		if (backward.isPressed())
			moveVec.z++;
		if (left.isPressed())
			moveVec.x--;
		if (right.isPressed())
			moveVec.x++;
	
		float distSqr = moveVec.lengthSqr();
		if (!MathUtils.nearZero(distSqr)) {
			if (!MathUtils.nearZero(1.0f - distSqr))
				moveVec.div(MathUtils.sqrt(distSqr));
			
			rotMat.setRotation(player.pitch, 0.0f, 1.0f, 0.0f);
			rotMat.mul(moveVec, moveVec);

			moved = true;
		}
		
		if (player.onGround && jump.isActive())
			moveVec.y = 2.0f;
	}

	public boolean hasMoved() {
		return moved;
	}
	
	public boolean isSneaking() {
		return sneaking;
	}
	
	public float getMoveX() {
		return moveVec.x;
	}

	public float getMoveY() {
		return moveVec.y;
	}
	
	public float getMoveZ() {
		return moveVec.z;
	}
}
