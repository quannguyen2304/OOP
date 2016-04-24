package core;

public class State implements IState {
	private boolean initial, terminal;

	public State(boolean initial, boolean terminal) {
		this.initial = initial;
		this.terminal = terminal;
	}

	@Override
	public boolean initial() {
		// TODO Auto-generated method stub
		return initial;
	}

	@Override
	public boolean terminal() {
		// TODO Auto-generated method stub
		return terminal;
	}

	public void setInitial(boolean initial) {
		this.initial = initial;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	
	public boolean getInitial() {
		return this.initial;
	}

	public boolean getTerminal() {
		return this.terminal;
	}

	public void print(){
		System.out.println("Initial "+ initial + ", Terminal "+ terminal);
	}
}
