package mineclone.client;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import mineclone.client.controller.PlayerController;
import mineclone.client.graphic.Display;
import mineclone.client.graphic.DisplayListener;
import mineclone.client.graphic.DisplaySize;
import mineclone.client.input.IKeyboardListener;
import mineclone.client.input.Keyboard;
import mineclone.client.input.Mouse;
import mineclone.client.renderer.world.BlockTextures;
import mineclone.client.renderer.world.WorldRenderer;
import mineclone.client.world.ClientWorld;
import mineclone.common.TickTimer;

public class MinecloneClient implements DisplayListener, IKeyboardListener {

	private static final String TITLE = "Mineclone";
	private static final int DEFAULT_WIDTH  = 856;
	private static final int DEFAULT_HEIGHT = 480;
	
	private static final float TPS = 20.0f;
	
	private Display display;
	
	private PlayerController controller;

	private ClientWorld world;
	private WorldRenderer worldRenderer;
	
	private TickTimer timer;
	
	private MinecloneClient() {
	}
	
	private void start() {
		display = new Display();
		display.initDisplay(TITLE, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		Mouse.init(display);
		Keyboard.init(display);
		
		Keyboard.addListener(this);
		
		display.setMouseGrabbed(true);
		
		run();
		stop();
	}

	private void init() {
		controller = new PlayerController(display);

		world = new ClientWorld(this);
		worldRenderer = new WorldRenderer(world);
		
		timer = new TickTimer(TPS);

		world.generateWorld();
	}
	
	private void initGLState() {
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		glDisable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		// TODO: replace clear color with skybox
		glClearColor(WorldRenderer.SKY_COLOR.getRedN(),
		             WorldRenderer.SKY_COLOR.getGreenN(), 
		             WorldRenderer.SKY_COLOR.getBlueN(), 
		             WorldRenderer.SKY_COLOR.getAlphaN());
	}
	
	public void run() {
		init();
		
		DisplaySize size = display.getDisplaySize();
		sizeChanged(size.width, size.height);
		
		display.addDisplayListener(this::sizeChanged);

		initGLState();

		timer.init();
		
		long last = System.currentTimeMillis();
		int frames = 0;
		
		while (!display.isCloseRequested()) {
			int ticksThisFrame = timer.clock();
			
			for (int i = 0; i < ticksThisFrame; i++)
				tick();
			
			render(timer.getDeltaTick());

			frames++;
			
			long now = System.currentTimeMillis();
			if (now - last >= 1000L) {
				last += 1000L;
				
				System.out.println("FPS: " + frames);
				
				frames = 0;
			}
			
			display.update();
		}
	}
	
	private void stop() {
		worldRenderer.close();
		
		// TODO: replace this with an asset manager
		BlockTextures.blocksTexture.close();
		
		display.close();
	}
	
	@Override
	public void sizeChanged(int width, int height) {
		glViewport(0, 0, width, height);
		
		worldRenderer.displaySizeChanged(width, height);
	}
	
	private void tick() {
		world.update();
		
		worldRenderer.update();
	}

	private void render(float dt) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		worldRenderer.render(dt);
	}

	public WorldRenderer getWorldRenderer() {
		return worldRenderer;
	}

	public PlayerController getController() {
		return controller;
	}
	
	@Override
	public void keyPressed(int key, int mods) {
		if (key == Keyboard.KEY_F11)
			display.setFullScreen(!display.isFullScreen());
	}

	@Override
	public void keyRepeated(int key, int mods) {
	}

	@Override
	public void keyReleased(int key, int mods) {
	}

	@Override
	public void keyTyped(int codePoint) {
	}
	
	public static void main(String[] args) throws Exception {
		new MinecloneClient().start();
	}
}
