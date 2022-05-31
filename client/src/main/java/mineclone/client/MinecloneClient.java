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
import mineclone.client.net.ClientNetworkSocket;
import mineclone.client.net.IntegratedNetworkConnection;
import mineclone.client.net.handler.ClientPacketHandler;
import mineclone.client.renderer.world.BlockTextures;
import mineclone.client.renderer.world.WorldRenderer;
import mineclone.client.world.ClientWorld;
import mineclone.common.TaskScheduler;
import mineclone.common.TickTimer;
import mineclone.common.net.INetworkConnection;
import mineclone.common.net.INetworkListener;
import mineclone.common.net.NetworkManager;
import mineclone.common.net.NetworkSide;
import mineclone.common.net.packet.IPacket;
import mineclone.common.net.packet.c2s.PlayerJoinC2SPacket;
import mineclone.common.util.DebugUtil;
import mineclone.common.world.IClientWorld;

public class MinecloneClient implements DisplayListener, IKeyboardListener, INetworkListener {

	private static final String TITLE = "Mineclone";
	private static final int DEFAULT_WIDTH  = 856;
	private static final int DEFAULT_HEIGHT = 480;
	
	private static final boolean CONNECT_INTEGRATED = true;
	private static final String SERVER_HOSTNAME = "localhost";
	private static final int SERVER_PORT = 2202;
	
	private Display display;

	private TickTimer timer;
	private TaskScheduler taskScheduler;
	
	private PlayerController controller;

	private ClientWorld world;
	private WorldRenderer worldRenderer;
	
	private IntegratedMinecloneServer integratedServer;
	private ClientNetworkSocket socket;
	private NetworkManager networkManager;
	private INetworkConnection outgoingConnection;
	
	private MinecloneClient() {
	}
	
	private void start() {
		display = new Display();
		display.initDisplay(TITLE, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		taskScheduler = new TaskScheduler();
		
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
		
		networkManager = new NetworkManager(NetworkSide.CLIENT);
		networkManager.addListener(this);
	
		connectToServer();
	}
	
	private void connectToServer() {
		if (CONNECT_INTEGRATED) {
			integratedServer = new IntegratedMinecloneServer();
			try {
				integratedServer.startAsync();
			} catch (InterruptedException e) {
				throw new RuntimeException("Unable to start integrated server.", e);
			}
			
			IntegratedNetworkConnection ingoingConnection = new IntegratedNetworkConnection();
			INetworkConnection connection = integratedServer.connect(ingoingConnection);
			ingoingConnection.setEndpoint(new ClientPacketHandler(this, connection));
			networkManager.addConnection(connection);
		} else {
			socket = new ClientNetworkSocket(this, networkManager);
			try {
				socket.connect(SERVER_HOSTNAME, SERVER_PORT);
			} catch (Exception e) {
				socket.close();
				throw new RuntimeException("Unable to connect to dedicated server.", e);
			}
		}
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
	
	private void run() {
		init();
		
		DisplaySize size = display.getDisplaySize();
		sizeChanged(size.width, size.height);
		
		display.addDisplayListener(this::sizeChanged);

		initGLState();

		timer = new TickTimer();
		timer.init();
		
		long last = System.currentTimeMillis();
		int frames = 0;
		
		while (!display.isCloseRequested()) {
			int ticksThisFrame = timer.clock();
			for (int i = 0; i < ticksThisFrame; i++)
				tick();
			
			render(timer.getDeltaTick());

			if (DebugUtil.PRINT_FPS_AND_TPS) {
				frames++;
				
				long now = System.currentTimeMillis();
				if (now - last >= 1000L) {
					last += 1000L;
					
					System.out.println("[Client]: " + frames + " fps");
					
					frames = 0;
				}
			}
			
			taskScheduler.executeAll();
			
			display.update();
		}
	}
	
	private void stop() {
		networkManager.close();
		
		if (integratedServer != null) {
			integratedServer.requestStop();
			integratedServer = null;
		}
		
		if (socket != null) {
			socket.close();
			socket = null;
		}
		
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
		networkManager.update();
		
		world.update();
		worldRenderer.update();
	}

	private void render(float dt) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		worldRenderer.render(dt);
	}
	
	public void send(IPacket packet) {
		if (outgoingConnection != null)
			outgoingConnection.send(packet);
	}

	public TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}
	
	public WorldRenderer getWorldRenderer() {
		return worldRenderer;
	}

	public PlayerController getController() {
		return controller;
	}
	
	public IClientWorld getWorld() {
		return world;
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
	
	@Override
	public void connectionAdded(INetworkConnection connection) {
		if (outgoingConnection != null)
			throw new IllegalStateException("Already connected");

		outgoingConnection = connection;
		
		System.out.println("[Client]: Connected to server!");
		
		send(new PlayerJoinC2SPacket());
	}

	@Override
	public void connectionRemoved(INetworkConnection connection) {
		if (outgoingConnection == null)
			throw new IllegalArgumentException("Not connected");
		
		outgoingConnection = null;

		System.out.println("[Client]: Disconnected from server!");
	}
	
	public static void main(String[] args) throws Exception {
		new MinecloneClient().start();
	}
}
