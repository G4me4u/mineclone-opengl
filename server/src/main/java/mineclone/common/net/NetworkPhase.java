package mineclone.common.net;

public enum NetworkPhase {

	HANDSHAKE("Handshake", 0),
	LOAD("Load", 1),
	GAMEPLAY("Gameplay", 2);
	
	private static final NetworkPhase[] PHASES;
	
	static {
		PHASES = new NetworkPhase[values().length];
		
		for (NetworkPhase phase : values())
			PHASES[phase.index] = phase;
	}
	
	private final String name;
	private final int index;
	
	private NetworkPhase(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	public String getName() {
		return name;
	}
	
	public int getIndex() {
		return index;
	}
}
