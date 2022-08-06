package mineclone.common.world;

import mineclone.common.world.block.state.IIndexedValue;

public enum Axis implements IIndexedValue {

	X("x", 0, true) {

		@Override
		public int choose(int x, int y, int z) {
			return x;
		}
	},
	Y("y", 1, false) {

		@Override
		public int choose(int x, int y, int z) {
			return y;
		}
	},
	Z("z", 2, true) {

		@Override
		public int choose(int x, int y, int z) {
			return z;
		}
	};

	public static final Axis[] ALL;

	static {

		Axis[] values = values();
		ALL = new Axis[values.length];

		for (Axis axis : values) {
			ALL[axis.index] = axis;
		}
	}

	private final String name;
	private final int index;
	private final boolean horizontal;

	private Axis(String name, int index, boolean horizontal) {
		this.name = name;
		this.index = index;
		this.horizontal = horizontal;
	}

	public String getName() {
		return name;
	}

	@Override
	public int getIndex() {
		return index;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public abstract int choose(int x, int y, int z);

}
