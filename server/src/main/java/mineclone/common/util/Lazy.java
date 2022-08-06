package mineclone.common.util;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {

	private final Supplier<T> delegate;

	private T value;

	public Lazy(Supplier<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public T get() {
		return value == null ? (value = delegate.get()) : value;
	}
}
