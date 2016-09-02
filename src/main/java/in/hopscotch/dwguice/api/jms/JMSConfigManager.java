package in.hopscotch.dwguice.api.jms;

import io.dropwizard.lifecycle.Managed;

/**
 * 
 * @author amrish
 *
 */
public interface JMSConfigManager extends Managed {
	//void init(MessageQueueConfig config, Environment env);
	MessageSender createSender(String destination, boolean persistent);
	<T> MessageReceiverHandler registerReceiver(String destination, MessageReceiver<T> receiver, Class<? extends T> clazz,
            final boolean ackMessageOnException);
}
