package machine;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import core.DeterministicAutomaton;
import core.ITransition;
import core.State;
import core.Transition;

public class ObservableAutomaton<T> extends DeterministicAutomaton<T> {
	/*
	 * Declare params obs to decelerate Observable
	 */
	private Observable obs = new Observable(){
		@Override
		public void notifyObservers(Object obj){
			setChanged();
			super.notifyObservers(obj);
		}
	};
	
	/**
	 * Construct Observable Automaton
	 */
	public ObservableAutomaton(ArrayList<Transition<T>> transitions) throws Exception{
		super(transitions);
	}
	
	/**
	 * Create addObserver 
	 * @param Observer O
	 * @return void
	 */
	public void addObserver(Observer o){
		obs.addObserver(o);
	}
	
	/**
	 * Change current state
	 * @param Transition transition
	 * @return void
	 */
	@Override
	protected State changeCurrentState(ITransition<T> t){
		obs.notifyObservers(t);
		return (State) super.changeCurrentState(t);
	}
}

