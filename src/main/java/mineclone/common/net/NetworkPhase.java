package mineclone.common.net;

public enum NetworkPhase {

	HANDSHAKE(0),
	LOADING(1),
	GAMEPLAY(2);
	
	private static final NetworkPhase[] PHASES;
	
	static {
		PHASES = new NetworkPhase[values().length];
		
		for (NetworkPhase phase : values())
			PHASES[phase.index] = phase;
	}
	
	private final int index;
	
	private NetworkPhase(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public NetworkPhase getNext() {
		if (index + 1 >= PHASES.length)
			throw new IllegalStateException(this + " does not have a next phase.");
		
		return PHASES[index + 1];
	}
}
