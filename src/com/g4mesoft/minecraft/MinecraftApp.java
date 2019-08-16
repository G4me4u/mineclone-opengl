package com.g4mesoft.minecraft;

import com.g4mesoft.Application;
import com.g4mesoft.graphic.Display;
import com.g4mesoft.graphic.DisplayMode;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.graphics3d.AbstractPixelRenderer3D;
import com.g4mesoft.graphics3d.PixelRenderer3D;
import com.g4mesoft.input.key.KeyInput;
import com.g4mesoft.input.key.KeySingleInput;
import com.g4mesoft.minecraft.renderer.WorldRenderer;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.Block;
import com.sun.glass.events.KeyEvent;

public class MinecraftApp extends Application {

	private World world;
	private WorldRenderer worldRenderer;

	private KeyInput fullscreenKey;
	
	@Override
	public void init() {
		super.init();
	
		setMouseGrabbed(true);
		
		Block.registerBlocks();
		
		world = new World(this);
		worldRenderer = new WorldRenderer(world);
	
		fullscreenKey = new KeySingleInput("fullscreen", KeyEvent.VK_F11);
		addKey(fullscreenKey);
	}

	@Override
	protected void displayResized(int newWidth, int newHeight) {
		super.displayResized(newWidth, newHeight);
		
		int s = 0;
		newWidth >>>= s;
		newHeight >>>= s;
		
		Display display = getDisplay();
		
		IRenderer2D renderer = display.getRenderer();
		if (renderer instanceof PixelRenderer3D) {
			((PixelRenderer3D)renderer).setSize(newWidth, newHeight);
		} else {
			display.setRenderer(new PixelRenderer3D(display, newWidth, newHeight));
		}
		
		worldRenderer.displayResized(newWidth, newHeight);
	}
	
	@Override
	protected void tick() {
		world.update();
		
		if (fullscreenKey.isClicked()) {
			Display display = getDisplay();
			if (display.isFullscreen()) {
				display.setDisplayMode(DisplayMode.NORMAL);
			} else {
				display.setDisplayMode(DisplayMode.FULLSCREEN_BORDERLESS);
			}
		}
	}

	@Override
	protected void render(IRenderer2D renderer, float dt) {
		AbstractPixelRenderer3D renderer3D = (AbstractPixelRenderer3D)renderer;
		
		renderer3D.setColor(0xFFFFFF);
		renderer3D.clear();
		
		worldRenderer.render(renderer3D, dt);
	}

	public WorldRenderer getWorldRenderer() {
		return worldRenderer;
	}
	
	public static void main(String[] args) throws Exception {
		Application.start(args, MinecraftApp.class);
	}
}
