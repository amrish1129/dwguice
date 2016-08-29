package in.hopscotch.dwguice.jms.excception;

import javax.jms.Message;

public interface MessageQueueBaseExceptionHandler {
	boolean onException(Message jmsMessage, String message, Exception exception);
}
