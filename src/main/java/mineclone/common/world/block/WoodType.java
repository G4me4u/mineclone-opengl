package mineclone.common.world.block;

import mineclone.common.world.block.state.IIndexedValue;

public enum WoodType implements IIndexedValue {

	OAK("oak", 0),
	BIRCH("birch", 1),
	ACACIA("acacia", 2);

	public static final WoodType[] TYPES;

	static {
		TYPES = new WoodType[values().length];
		
		for (WoodType type : values())
			TYPES[type.index] = type;
	}
	
	private final String name;
	private final int index;
	
	private WoodType(String name, int index) {
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
