package minecraft.common.world.block;

import minecraft.common.world.block.state.IIndexedValue;

public enum StoneType implements IIndexedValue {

	STONE("stone", 0),
	COBBLESTONE("cobblestone", 1);
	
	public static final StoneType[] TYPES;
	
	static {
		TYPES = new StoneType[values().length];
		
		for (StoneType type : values())
			TYPES[type.index] = type;
	}
	
	private final String name;
	private final int index;
	
	private StoneType(String name, int index) {
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
