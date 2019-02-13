package com.g4mesoft.minecraft;

import com.g4mesoft.Application;
import com.g4mesoft.graphic.Display;
import com.g4mesoft.graphic.IRenderer2D;
import com.g4mesoft.graphics3d.AbstractPixelRenderer3D;
import com.g4mesoft.graphics3d.PixelRenderer3D;
import com.g4mesoft.minecraft.renderer.WorldRenderer;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.Block;

public class MinecraftApp extends Application {

	private static final int WORLD_WIDTH = 16 * 8;
	private static final int WORLD_DEPTH = 16 * 8;
	
	private World world;
	private WorldRenderer worldRenderer;
	
	@Override
	public void init() {
		super.init();
	
		setMouseGrabbed(true);
		
		Block.registerBlocks();
		
		world = new World(WORLD_WIDTH, WORLD_DEPTH);
		worldRenderer = new WorldRenderer(world);
	}

	@Override
	protected void displayResized(int newWidth, int newHeight) {
		super.displayResized(newWidth, newHeight);
		
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
	}

	@Override
	protected void render(IRenderer2D renderer, float dt) {
		AbstractPixelRenderer3D renderer3D = (AbstractPixelRenderer3D)renderer;
		
		renderer3D.setColor(0xFFFFFF);
		renderer3D.clear();
		
		worldRenderer.render(renderer3D, dt);
	}
	
	public static void main(String[] args) throws Exception {
		Application.start(args, MinecraftApp.class);
	}
}
