package core;

public interface ITransition<T> {

	public IState source();

	public IState target();

	public T label();
}