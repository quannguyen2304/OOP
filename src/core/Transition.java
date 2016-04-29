package core;

public class Transition<T> implements ITransition<T> {

	private State source;

	private State target;

	private T label;

	public Transition(State source, State target, T label) {
		this.source = source;
		this.target = target;
		this.label = label;
	}

	@Override
	public State source() {
		return source;
	}

	@Override
	public State target() {
		return target;
	}

	@Override
	public T label() {
		return label;
	}

}
