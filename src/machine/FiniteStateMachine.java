package machine;

import java.util.ArrayList;

import core.DeterministicAutomaton;
import core.IState;
import core.ITransition;
import core.Transition;

public class FiniteStateMachine<T> extends DeterministicAutomaton<T> {

	public FiniteStateMachine(ArrayList<Transition<T>> transitions) throws Exception {
		super(transitions);
		// TODO Auto-generated constructor stub		
	}
	
	@Override
	protected IState changeCurrentState(ITransition<T> t) {
		return ((TransitionWithAction<T>) t).cross();
	}
}
