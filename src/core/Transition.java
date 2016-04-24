package core;

public class Transition<T> implements ITransition<T> {

	private IState source;

	private IState target;

	private T label;

	public Transition(IState source, IState target, T label) {
		this.source = source;
		this.target = target;
		this.label = label;
	}

	@Override
	public IState source() {
		return source;
	}

	@Override
	public IState target() {
		return target;
	}

	@Override
	public T label() {
		return label;
	}

}
