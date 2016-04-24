package core;

public class NotDeterministInitialStateException extends Exception {
	private static final long serialVersionUID = 1L;

	private IState e1, e2;

	public NotDeterministInitialStateException(IState e1, IState e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public String getMessage() {
		return "Two initial states " + e1 + " and " + e2;
	}
}
