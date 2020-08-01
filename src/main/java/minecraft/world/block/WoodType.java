package minecraft.world.block;

public enum WoodType {

	OAK(0, "oak"),
	BIRCH(1, "birch"),
	ACACIA(2, "acacia");

	public static final WoodType[] WOOD_TYPES;

	static {
		WOOD_TYPES = new WoodType[values().length];
		
		for (WoodType type : values())
			WOOD_TYPES[type.getIndex()] = type;
	}
	
	private final int index;
	private final String name;
	
	private WoodType(int index, String name) {
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
