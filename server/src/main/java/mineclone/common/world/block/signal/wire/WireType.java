package mineclone.common.world.block.signal.wire;

import mineclone.common.world.block.signal.SignalType;

public enum WireType {

	REDSTONE (SignalType.REDSTONE),
	BLUESTONE(SignalType.BLUESTONE);

	private final SignalType signal;
	private final int min;
	private final int max;
	private final int step;

	private WireType(SignalType signal, int min, int max, int step) {
		if (signal == null || signal.isAny()) {
			throw new IllegalArgumentException("signal type cannot be null or any");
		}
		if (min < signal.min() || min > signal.max()) {
			throw new IllegalArgumentException("min must be bound by signal type's min/max");
		}
		if (max < signal.min() || max > signal.max()) {
			throw new IllegalArgumentException("max must be bound by signal type's min/max");
		}
		if (step < 0) {
			throw new IllegalArgumentException("step must be at least 0");
		}

		this.signal = signal;
		this.min = min;
		this.max = max;
		this.step = step;
	}

	private WireType(SignalType signal, int step) {
		this(signal, signal.min(), signal.max(), step);
	}

	private WireType(SignalType signal) {
		this(signal, 1);
	}

	public SignalType signal() {
		return signal;
	}

	public int min() {
		return min;
	}

	public int max() {
		return max;
	}

	public int step() {
		return step;
	}

	public int clamp(int signal) {
		return signal >= max ? max : (signal <= min ? min : signal);
	}

	public boolean isCompatible(WireType type) {
		return signal.is(type.signal);
	}
}
