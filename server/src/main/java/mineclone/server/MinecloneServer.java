package mineclone.server;

import mineclone.common.TaskScheduler;
import mineclone.common.TickTimer;
import mineclone.common.net.INetworkConnection;
import mineclone.common.net.INetworkListener;
import mineclone.common.net.NetworkManager;
import mineclone.common.net.NetworkSide;
import mineclone.common.net.packet.IPacket;
import mineclone.common.util.DebugUtil;
import mineclone.common.world.IServerWorld;
import mineclone.server.world.ServerWorld;

public abstract class MinecloneServer implements INetworkListener {

	private static final int TOTAL_WAIT_INTERVALS = 5;
	
	protected NetworkManager networkManager;
	
	private TaskScheduler taskScheduler;
	private TickTimer timer;
	private volatile boolean running;

	protected ServerWorld world;
	
	public void start() {
		networkManager = new NetworkManager(NetworkSide.SERVER);
		networkManager.addListener(this);

		taskScheduler = new TaskScheduler();
		
		world = new ServerWorld(this);
		
		run();
		stopAll();
	}
	
	protected abstract void init();
	
	private void run() {
		running = true;

		init();
		
		timer = new TickTimer();
		timer.init();
		
		long last = System.currentTimeMillis();
		int ticks = 0;

		int remainingWaits = 0;
		while (running) {
			int ticksThisFrame = timer.clock();
			for (int i = 0; i < ticksThisFrame; i++)
				tick();
			
			if (DebugUtil.PRINT_FPS_AND_TPS) {
				ticks += ticksThisFrame;
				
				long now = System.currentTimeMillis();
				if (now - last >= 1000L) {
					last += 1000L;
					
					System.out.println("[Server]: " + ticks + " tps");
					
					ticks = 0;
				}
			}
			
			// Account for inaccuracy of sleep by sleeping in
			// several intervals of approximately equal time.
			if (ticksThisFrame != 0)
				remainingWaits = TOTAL_WAIT_INTERVALS;
			
			long remainingMs = timer.remainingMillis();
			if (remainingWaits > 0) {
				remainingMs = (remainingMs - 1) / remainingWaits + 1; // ceilDiv(ms, waits)
				remainingWaits--;
			}
			
			taskScheduler.execute(remainingMs);
		}
	}
	
	protected abstract void stop();
	
	private void stopAll() {
		stop();
		
		networkManager.close();
	}
	
	protected void tick() {
		networkManager.tick();
		
		world.tick();
	}
	
	public void sendToAll(IPacket packet) {
		networkManager.forEach((connection) -> {
			connection.send(packet);
		});
	}
	
	public void requestStop() {
		running = false;
	}
	
	public TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}
	
	public IServerWorld getWorld() {
		return world;
	}
	
	@Override
	public void connectionAdded(INetworkConnection connection) {
		System.out.println("[Server]: Client connected");
	}
	
	@Override
	public void connectionRemoved(INetworkConnection connection) {
		System.out.println("[Server]: Client disconnected");
	}
}
