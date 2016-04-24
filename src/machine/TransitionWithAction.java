package machine;

import core.IState;
import core.ITransition;

public class TransitionWithAction<T> implements ITransition<T> {

	private final ITransition<T> transition;
	private final IAction<T> action;

	public TransitionWithAction(ITransition<T> t, IAction<T> a) {
		this.transition = t;
		this.action = a;
	}

	@Override
	public IState source() {
		// TODO Auto-generated method stub
		return transition.source();
	}

	@Override
	public IState target() {
		// TODO Auto-generated method stub
		return transition.target();
	}

	@Override
	public T label() {
		// TODO Auto-generated method stub
		return transition.label();
	}

	public IState cross() {
		action.execute(transition.label());
		return transition.target();
	}
}
