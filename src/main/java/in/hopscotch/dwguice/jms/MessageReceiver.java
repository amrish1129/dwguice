package in.hopscotch.dwguice.jms;

public interface MessageReceiver<T> {
	public void receive(T message);
}
