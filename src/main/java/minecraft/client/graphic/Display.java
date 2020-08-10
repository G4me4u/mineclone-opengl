package minecraft.client.graphic;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import minecraft.common.IResource;

public class Display implements IResource {

	private long windowHandle;
	
	private List<DisplayListener> listeners;
	
	private boolean supportsRawMouseMotion;
	private boolean mouseGrabbed;
	private Map<Integer, Long> standardCursors;
	
	private boolean fullScreen;
	private boolean vSync;
	
	private long windowMonitor;
	
	private int prevX;
	private int prevY;
	private int prevWidth;
	private int prevHeight;
	
	public Display() {
		windowHandle = -1L;
		
		listeners = new ArrayList<>();
	
		supportsRawMouseMotion = false;
		mouseGrabbed = false;
		standardCursors = new HashMap<>();

		fullScreen = false;
		vSync = false;
	}
	
	public void addDisplayListener(DisplayListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeDisplayListener(DisplayListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	public void initDisplay(String title, int width, int height) {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		supportsRawMouseMotion = glfwRawMouseMotionSupported();
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
		// Create the window
		windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
		if (windowHandle == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetWindowSizeCallback(windowHandle, (window, newWidth, newHeight) -> {
			// Get the frame buffer size instead of the provided newWidth, newHeight.
			DisplaySize newSize = getDisplaySize();
			
			dispatchSizeChangedEvent(newSize.width, newSize.height);
		});
		
		glfwSetMonitorCallback((monitor, event) -> {
			if (monitor == windowMonitor && event == GLFW_DISCONNECTED)
				setFullScreen(false);
		});
		
		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		DisplaySize size = getDisplaySize();
		
		// Center the window
		setDisplayPosition((vidmode.width()  - size.width ) / 2, 
		                   (vidmode.height() - size.height) / 2);
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(windowHandle);
		// Tell LWJGL to look for the OpenGL context.
		createCapabilities();
		
		// Enable v-sync
		setVSync(true);
		
		glfwShowWindow(windowHandle);
	}
	
	public void update() {
		glfwSwapBuffers(windowHandle);
		glfwPollEvents();
	}

	private void dispatchSizeChangedEvent(int width, int height) {
		for (DisplayListener listener : listeners)
			listener.sizeChanged(width, height);
	}
	
	public DisplaySize getDisplaySize() {
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*
	
			glfwGetFramebufferSize(windowHandle, pWidth, pHeight);
		
			return new DisplaySize(pWidth.get(0), pHeight.get(0));
		}
	}
	
	public void setDisplayPosition(int wx, int wy) {
		glfwSetWindowPos(windowHandle, wx, wy);
	}

	public void setKeyCallback(GLFWKeyCallbackI callback) {
		glfwSetKeyCallback(windowHandle, callback);
	}

	public void setCharCallback(GLFWCharCallbackI callback) {
		glfwSetCharCallback(windowHandle, callback);
	}
	
	public void setCursorPosCallback(GLFWCursorPosCallbackI callback) {
		glfwSetCursorPosCallback(windowHandle, callback);
	}

	public void setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
		glfwSetMouseButtonCallback(windowHandle, callback);
	}
	
	public void setScrollCallback(GLFWScrollCallbackI callback) {
		glfwSetScrollCallback(windowHandle, callback);
	}
	
	public void setWindowShouldClose(boolean value) {
		glfwSetWindowShouldClose(windowHandle, value);
	}
	
	public boolean isCloseRequested() {
		return glfwWindowShouldClose(windowHandle);
	}

	public void setCursor(int cursorType) {
		if (cursorType == GLFW_ARROW_CURSOR) {
			glfwSetCursor(windowHandle, NULL);
		} else {
			long ptr;
			if (standardCursors.containsKey(cursorType)) {
				ptr = standardCursors.get(cursorType);
			} else {
				ptr = glfwCreateStandardCursor(cursorType);
				standardCursors.put(cursorType, ptr);
			}
			
			glfwSetCursor(windowHandle, ptr);
		}
	}

	public String getClipboardString() {
		return glfwGetClipboardString(windowHandle);
	}

	public void setClipboardString(String clipboardString) {
		glfwSetClipboardString(windowHandle, clipboardString);
	}
	
	public boolean isMouseGrabbed() {
		return mouseGrabbed;
	}
	
	public void setMouseGrabbed(boolean grabbed) {
		if (grabbed != mouseGrabbed) {
			if (grabbed) {
				glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
				
				if (supportsRawMouseMotion)
					glfwSetInputMode(windowHandle, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
			} else {
				glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
				
				if (supportsRawMouseMotion)
					glfwSetInputMode(windowHandle, GLFW_RAW_MOUSE_MOTION, GLFW_FALSE);
			}

			mouseGrabbed = grabbed;
		}
	}
	
	public boolean isFullScreen() {
		return fullScreen;
	}
	
	public void setFullScreen(boolean fullScreen) {
		if (fullScreen != this.fullScreen) {
			if (fullScreen) {
				long monitor = glfwGetPrimaryMonitor();
				GLFWVidMode mode = glfwGetVideoMode(monitor);
				
				try (MemoryStack stack = stackPush()) {
					IntBuffer pX = stack.mallocInt(1); // int*
					IntBuffer pY = stack.mallocInt(1); // int*
					
					glfwGetWindowPos(windowHandle, pX, pY);
					
					prevX = pX.get(0);
					prevY = pY.get(0);

					glfwGetWindowSize(windowHandle, pX, pY);
					
					prevWidth = pX.get(0);
					prevHeight = pY.get(0);
				}

				glfwSetWindowMonitor(windowHandle, monitor, 0, 0, mode.width(), mode.height(), mode.refreshRate());
				
				windowMonitor = monitor;
			} else {
				glfwSetWindowMonitor(windowHandle, NULL, prevX, prevY, prevWidth, prevHeight, GLFW_DONT_CARE);

				windowMonitor = NULL;
			}

			this.fullScreen = fullScreen;

			setVSync(vSync);
		}
	}
	
	public boolean isVSync() {
		return vSync;
	}
	
	public void setVSync(boolean vSync) {
		this.vSync = vSync;
		
		glfwSwapInterval(vSync ? 1 : 0);
	}

	public long getHandle() {
		return windowHandle;
	}
	
	@Override
	public void close() {
		// Destroy the standard cursors
		for (Long cursorPtr : standardCursors.values())
			glfwDestroyCursor(cursorPtr.longValue());
		standardCursors.clear();
		
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(windowHandle);
		glfwDestroyWindow(windowHandle);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
}
