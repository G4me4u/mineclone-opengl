package mineclone.common.world.block;

import mineclone.common.world.block.state.IIndexedValue;

public enum WireConnection implements IIndexedValue {

	NONE("none", 0),
	SIDE("side", 1),
	UP  ("up"  , 2);

	public static final WireConnection[] ALL;

	static {
		ALL = new WireConnection[values().length];

		for (WireConnection connection : values())
			ALL[connection.index] = connection;
	}

	private final String name;
	private final int index;

	private WireConnection(String name, int index) {
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
