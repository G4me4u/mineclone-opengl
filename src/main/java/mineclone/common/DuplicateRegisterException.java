package mineclone.common;

@SuppressWarnings("serial")
public class DuplicateRegisterException extends RuntimeException {

	public DuplicateRegisterException() {
	}

	public DuplicateRegisterException(String msg) {
		super(msg);
	}
}
