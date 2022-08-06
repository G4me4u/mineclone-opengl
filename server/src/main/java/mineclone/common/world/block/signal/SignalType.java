package mineclone.common.world.block.signal;

public enum SignalType {

	ANY(Integer.MIN_VALUE, Integer.MAX_VALUE),

	REDSTONE (0, 15),
	BLUESTONE(0, 15);

	private final int min;
	private final int max;

	private SignalType(int min, int max) {
		if (max < min) {
			throw new IllegalArgumentException("max cannot be less than min");
		}

		this.min = min;
		this.max = max;
	}

	public final int min() {
		return min;
	}

	public final int max() {
		return max;
	}

	public final int clamp(int signal) {
		return signal >= max ? max : (signal <= min ? min : signal);
	}

	public final boolean isAny() {
		return this == ANY;
	}

	public final boolean is(SignalType type) {
		return isAny() || type.isAny() || this == type;
	}
}
