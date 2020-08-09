package minecraft.common.world;

public enum Axis {
	
	X("x", 0, true),
	Y("y", 1, false),
	Z("z", 2, true);
	
	public static final Axis[] AXES;
	
	static {
		AXES = new Axis[values().length];
		
		for (Axis axis : values())
			AXES[axis.index] = axis;
	}
	
	private final String name;
	private final int index;

	private boolean horizontal;
	
	private Axis(String name, int index, boolean horizontal) {
		this.name = name;
		this.index = index;

		this.horizontal = horizontal;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isHorizontal() {
		return horizontal;
	}
}
