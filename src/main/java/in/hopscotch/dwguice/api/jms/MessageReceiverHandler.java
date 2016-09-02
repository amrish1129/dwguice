package in.hopscotch.dwguice.api.jms;

import javax.jms.Message;
import javax.jms.MessageConsumer;

public interface MessageReceiverHandler {
	void processMessage(MessageConsumer rawMessageConsumer, Message message) throws Exception;
}
