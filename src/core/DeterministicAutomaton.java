package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class DeterministicAutomaton<T> {

	private IState initialState = null;

	/*
	 * In the map transitions, at each state s we associate a map m where the
	 * values are the transitions having s as source and the corresponding key
	 * the labels of the transitions.
	 * 
	 */
	private final Map<IState, Map<T, ITransition<T>>> transitions;

	@SuppressWarnings("unchecked")
	public DeterministicAutomaton(ArrayList<Transition<T>> transitions) throws Exception {
		this.transitions = new HashMap<IState, Map<T, ITransition<T>>>();
		Iterator<T> iterTrans = (Iterator<T>) transitions.iterator();
		while (iterTrans.hasNext()) {
			Transition<T> t = (Transition<T>) iterTrans.next();
			addState(t.source());
			addState(t.target());
			
			Map<T, ITransition<T>> map = this.transitions.get(t.source());
		
			if(map.containsKey(t.label())){ 
				throw new NotDeterministTransitionException("Not Derterminist Transition");
			}
										
			map.put(t.label(), t);
		}
		
		if(initialState == null){
			throw new UnknownInitialStateException("Not Determinist Inital State");
		}
	}
	
	protected final void addState(IState s) throws NotDeterministInitialStateException {		
		if (!this.transitions.containsKey(s)) {
			this.transitions.put(s, new HashMap<T, ITransition<T>>());
			if (s.initial()) {
				if (this.initialState == null) {
					this.initialState = s;
				} else {
					throw new NotDeterministInitialStateException(s, this.initialState);
				}
			}
		}
	}

	public IState initialState() {
		return initialState;
	}

	public ITransition<T> transition(IState s, T label) {
		if (!transitions.containsKey(s)) {
			throw new NoSuchElementException();
		}
		return transitions.get(s).get(label);
	}

	public boolean recognize(@SuppressWarnings("unchecked") T... word) {
		return recognize(Arrays.asList(word).iterator());
	}

	public boolean recognize(Iterator<T> word) {
		IState s = initialState;
		while (word.hasNext()) {
			ITransition<T> t = transition(s, word.next());
			if (t == null) {
				return false;
			} else {
				s = changeCurrentState(t);
			}
		}
		return s.terminal();
	}

	protected IState changeCurrentState(ITransition<T> t) {
		return t.target();
	}
}