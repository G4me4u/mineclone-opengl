package mineclone.common;

public class TickTimer {

	public static final float MILLIS_PER_SECOND = 1000.0f;
	public static final float DEFAULT_TPS = 20.0f;
	
	private final float tps;
	private final float millisPerTick;
	
	private long last;
	private float dt;

	public TickTimer() {
		this(DEFAULT_TPS);
	}
	
	public TickTimer(float tps) {
		this.tps = tps;
		
		millisPerTick = MILLIS_PER_SECOND / tps;
	}
	
	public void init() {
		last = System.currentTimeMillis();
		dt = 1.0f;
	}
	
	public int clock() {
		long now = System.currentTimeMillis();
		long deltaMs = now - last;
		last = now;
		
		dt += deltaMs / millisPerTick;
		
		int ticksThisFrame = (int)dt;
		dt -= ticksThisFrame;
		
		return ticksThisFrame;
	}
	
	public long remainingMillis() {
		return (long)(millisPerTick * (1.0f - dt)) + 1;
	}
	
	public float getDeltaTick() {
		return dt;
	}
	
	public float getTps() {
		return tps;
	}
}
