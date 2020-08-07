package minecraft.client.input;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.glfw.GLFW;

import minecraft.client.graphic.Display;

public class Mouse {

	public static final int UNKNOWN_BUTTON = -1;
	
	public static final int BUTTON_1 = GLFW.GLFW_MOUSE_BUTTON_1;
	public static final int BUTTON_2 = GLFW.GLFW_MOUSE_BUTTON_2;
	public static final int BUTTON_3 = GLFW.GLFW_MOUSE_BUTTON_3;
	public static final int BUTTON_4 = GLFW.GLFW_MOUSE_BUTTON_4;
	public static final int BUTTON_5 = GLFW.GLFW_MOUSE_BUTTON_5;
	public static final int BUTTON_6 = GLFW.GLFW_MOUSE_BUTTON_6;
	public static final int BUTTON_7 = GLFW.GLFW_MOUSE_BUTTON_7;
	public static final int BUTTON_8 = GLFW.GLFW_MOUSE_BUTTON_8;

	public static final int BUTTON_LEFT   = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	public static final int BUTTON_MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	public static final int BUTTON_RIGHT  = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	
	public static final int FIRST_BUTTON = GLFW.GLFW_MOUSE_BUTTON_1;
	public static final int LAST_BUTTON  = GLFW.GLFW_MOUSE_BUTTON_8;

	private static Mouse instance;
	
	private final Display display;

	private final List<IMouseListener> listeners;
	private final Set<Integer> heldButtons;
	
	private float mouseX;
	private float mouseY;
	
	private float deltaX;
	private float deltaY;
	
	private Mouse(Display display) {
		this.display = display;
		
		listeners = new ArrayList<>();
		
		heldButtons = new LinkedHashSet<>();
	}
	
	public static void init(Display display) {
		if (instance == null) {
			instance = new Mouse(display);
			instance.initImpl();
		}
	}
	
	public static void addListener(IMouseListener listener) {
		instance.addListenerImpl(listener);
	}

	public void removeListener(IMouseListener listener) {
		instance.removeListenerImpl(listener);
	}
	
	public static boolean isHeld(int button) {
		return instance.isHeldImpl(button);
	}
	
	public static float getMouseX() {
		return instance.getMouseXImpl();
	}

	public static float getMouseY() {
		return instance.getMouseYImpl();
	}
	
	public static float getDeltaX() {
		return instance.getDeltaXImpl();
	}

	public static float getDeltaY() {
		return instance.getDeltaYImpl();
	}
	
	private void initImpl() {
		display.setCursorPosCallback(this::glfwCursorPosCallback);
		display.setMouseButtonCallback(this::glfwMouseButtonCallback);
		display.setScrollCallback(this::glfwScrollCallback);
	}
	
	private void addListenerImpl(IMouseListener listener) {
		listeners.add(listener);
	}

	private void removeListenerImpl(IMouseListener listener) {
		listeners.remove(listener);
	}
	
	private boolean isHeldImpl(int button) {
		return heldButtons.contains(button);
	}
	
	private float getMouseXImpl() {
		return mouseX;
	}

	private float getMouseYImpl() {
		return mouseY;
	}
	
	private float getDeltaXImpl() {
		return deltaX;
	}

	private float getDeltaYImpl() {
		return deltaY;
	}
	
	private void glfwCursorPosCallback(long window, double xpos, double ypos) {
		deltaX = (float)xpos - mouseX;
		deltaY = (float)ypos - mouseY;
		
		mouseX = (float)xpos;
		mouseY = (float)ypos;
	
		dispatchMouseMovedEvent(mouseX, mouseY);
		
		for (Integer pressedButton : heldButtons)
			dispatchMouseDraggedEvent(pressedButton, mouseX, mouseY, deltaX, deltaY);
	}
	
	private void glfwMouseButtonCallback(long window, int button, int action, int mods) {
		switch (action) {
		case GLFW.GLFW_PRESS:
			dispatchMousePressedEvent(button, mouseX, mouseY, mods);
			heldButtons.add(button);
			break;
		case GLFW.GLFW_RELEASE:
			dispatchMouseReleasedEvent(button, mouseX, mouseY, mods);
			heldButtons.remove(button);
			break;
		}
	}

	private void glfwScrollCallback(long window, double xoffset, double yoffset) {
		dispatchMouseScrollEvent(mouseX, mouseY, (float)xoffset, (float)yoffset);
	}
	
	private void dispatchMouseMovedEvent(float mouseX, float mouseY) {
		for (IMouseListener listener : listeners)
			listener.mouseMoved(mouseX, mouseY);
	}	
	
	private void dispatchMouseDraggedEvent(int button, float mouseX, float mouseY, float dragX, float dragY) {
		for (IMouseListener listener : listeners)
			listener.mouseDragged(button, mouseX, mouseY, dragX, dragY);
	}

	private void dispatchMousePressedEvent(int button, float mouseX, float mouseY, int modifiers) {
		for (IMouseListener listener : listeners)
			listener.mousePressed(button, mouseX, mouseY, modifiers);
	}

	private void dispatchMouseReleasedEvent(int button, float mouseX, float mouseY, int modifiers) {
		for (IMouseListener listener : listeners)
			listener.mouseReleased(button, mouseX, mouseY, modifiers);
	}

	private void dispatchMouseScrollEvent(float mouseX, float mouseY, float scrollX, float scrollY) {
		for (IMouseListener listener : listeners)
			listener.mouseScroll(mouseX, mouseY, scrollX, scrollY);
	}
}
