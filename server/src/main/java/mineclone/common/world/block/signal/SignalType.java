package mineclone.common.world.block.signal;

public enum SignalType {

	ANY(Integer.MIN_VALUE, Integer.MAX_VALUE),

	REDSTONE (0, 15),
	BLUESTONE(0, 15);

	private final int min;
	private final int max;

	private SignalType(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public boolean isAny() {
		return this == ANY;
	}

	public boolean is(SignalType type) {
		return isAny() || type.isAny() || this == type;
	}

	public int min() {
		return min;
	}

	public int max() {
		return max;
	}

	public int clamp(int signal) {
		return signal >= max ? max : (signal <= min ? min : signal);
	}
}
