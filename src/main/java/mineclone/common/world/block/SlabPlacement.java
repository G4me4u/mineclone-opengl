package mineclone.common.world.block;

import mineclone.common.world.block.state.IIndexedValue;

public enum SlabPlacement implements IIndexedValue {

	TOP("top", 0),
	BOTTOM("bottom", 1),
	BOTH("double", 2);
	
	public static final SlabPlacement[] PLACEMENTS;
	
	static {
		PLACEMENTS = new SlabPlacement[values().length];
		
		for (SlabPlacement type : values())
			PLACEMENTS[type.index] = type;
	}
	
	private final String name;
	private final int index;
	
	private SlabPlacement(String name, int index) {
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
