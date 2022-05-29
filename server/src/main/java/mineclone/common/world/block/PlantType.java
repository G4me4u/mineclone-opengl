package mineclone.common.world.block;

import mineclone.common.world.block.state.IIndexedValue;

public enum PlantType  implements IIndexedValue {

	GRASS("grass", 0),
	FORGETMENOT("forgetmenot", 1),
	MARIGOLD("marigold", 2),
	DAISY("daisy", 3);

	public static final PlantType[] TYPES;

	static {
		TYPES = new PlantType[values().length];
		
		for (PlantType type : values())
			TYPES[type.index] = type;
	}
	
	private final String name;
	private final int index;
	
	private PlantType(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int getIndex() {
		return index;
	}
}
