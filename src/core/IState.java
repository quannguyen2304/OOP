package core;

public interface IState {
	public boolean initial();

	public boolean terminal();
	
	public void setInitial(boolean initial);
	
	public void setTerminal(boolean terminal);
}