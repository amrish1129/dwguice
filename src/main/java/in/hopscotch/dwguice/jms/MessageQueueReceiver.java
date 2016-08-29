package in.hopscotch.dwguice.jms;

public interface MessageQueueReceiver<T> {
	public void receive(T message);
}
