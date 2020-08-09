package minecraft.common.world.block;

public enum WireConnection {
	
	NONE(0, "none"),
	SIDE(1, "side"),
	UP(2, "up");
	
	public static final WireConnection[] CONNECTIONS;
	
	static {
		CONNECTIONS = new WireConnection[values().length];
		
		for (WireConnection connection : values())
			CONNECTIONS[connection.index] = connection;
	}
	
	private final int index;
	private final String name;
	
	private WireConnection(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
}
