package machine;

import java.util.Observable;
import java.util.Observer;

import core.Transition;

public class ChangeTransition implements Observer {

	@SuppressWarnings("rawtypes")
	public void update(Observable o, Object arg) {
		System.out.println("Recognized string "+ ((Transition)arg).label());
	}

}
