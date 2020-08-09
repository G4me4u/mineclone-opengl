package minecraft.common.world.block;

public enum WireConnection {
	
	NONE("none"),
	SIDE("side"),
	UP("up");
	
	private final String name;
	
	private WireConnection(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
