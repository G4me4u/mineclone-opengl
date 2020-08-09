package minecraft.common.world.block;

public enum PlantType {

	GRASS(0, "grass"),
	FORGETMENOT(1, "forgetmenot"),
	MARIGOLD(2, "marigold"),
	DAISY(3, "daisy");

	public static final PlantType[] TYPES;

	static {
		TYPES = new PlantType[values().length];
		
		for (PlantType type : values())
			TYPES[type.getIndex()] = type;
	}
	
	private final int index;
	private final String name;
	
	private PlantType(int index, String name) {
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
