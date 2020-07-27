package com.g4mesoft.minecraft.world.block;

public enum PlantType {

	GRASS(0, "grass"),
	FORGETMENOT(1, "forgetmenot"),
	MARIGOLD(2, "marigold"),
	DAISY(3, "daisy");

	public static final PlantType[] PLANT_TYPES;

	static {
		PLANT_TYPES = new PlantType[values().length];
		
		for (PlantType type : values())
			PLANT_TYPES[type.getIndex()] = type;
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
