package in.hopscotch.dwguice.api.jms;

/**
 * 
 * @author amrish
 *
 * @param <T>
 */
public interface MessageReceiver<T> {
	public void receive(T message);
}
