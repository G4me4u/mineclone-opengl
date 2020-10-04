package mineclone.client.controller;

import mineclone.client.graphic.Display;
import mineclone.client.input.IMouseListener;
import mineclone.client.input.Keyboard;
import mineclone.client.input.Mouse;
import mineclone.common.math.LinMath;
import mineclone.common.math.Mat3;
import mineclone.common.math.Vec3;
import mineclone.common.world.entity.PlayerEntity;

public class PlayerController implements IMouseListener {

	private static final float MOUSE_SENSITIVITY = 0.2f;
	
	private final Display display;
	
	private final Vec3 moveVec;
	private final Mat3 rotMat;

	private PlayerEntity player;
	
	public PlayerController(Display display) {
		this.display = display;
		
		moveVec = new Vec3();
		rotMat = new Mat3();

		player = null;
		
		Mouse.addListener(this);
	}
	
	public void setPlayer(PlayerEntity player) {
		this.player = player;
	}
	
	public void update() {
		if (player != null) {
			moveVec.set(0.0f);
			
			if (Keyboard.isHeld(Keyboard.KEY_W))
				moveVec.z--;
			if (Keyboard.isHeld(Keyboard.KEY_S))
				moveVec.z++;
			if (Keyboard.isHeld(Keyboard.KEY_A))
				moveVec.x--;
			if (Keyboard.isHeld(Keyboard.KEY_D))
				moveVec.x++;
	
			float distSqr = moveVec.lengthSqr();
			
			if (!LinMath.nearZero(distSqr)) {
				moveVec.div((float)Math.sqrt(distSqr));
				
				rotMat.toIdentity();
				rotMat.rotateY(player.getPitch());
				rotMat.mul(moveVec, moveVec);
			}
			
			if (player.onGround && Keyboard.isHeld(Keyboard.KEY_SPACE))
				moveVec.y = 2.0f;
			
			float vx = moveVec.x * 0.1f;
			float vy = moveVec.y * 0.285f;
			float vz = moveVec.z * 0.1f;
			
			player.addVelocity(vx, vy, vz);
		}
	}
	
	@Override
	public void mouseMoved(float x, float y) {
		if (player != null && display.isMouseGrabbed()) {
			float deltaX = Mouse.getDeltaX() * MOUSE_SENSITIVITY;
			float deltaY = Mouse.getDeltaY() * MOUSE_SENSITIVITY;
			
			player.setYaw(player.getYaw() - deltaY * LinMath.DEG_TO_RAD);
			player.setPitch(player.getPitch() - deltaX * LinMath.DEG_TO_RAD);
		}
	}
	
	@Override
	public void mouseDragged(int button, float mouseX, float mouseY, float dragX, float dragY) {
	}

	@Override
	public void mousePressed(int button, float mouseX, float mouseY, int modifiers) {
	}

	@Override
	public void mouseReleased(int button, float mouseX, float mouseY, int modifiers) {
	}

	@Override
	public void mouseScroll(float mouseX, float mouseY, float scrollX, float scrollY) {
	}
	
	public PlayerEntity getPlayer() {
		return player;
	}
}
