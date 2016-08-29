package in.hopscotch.dwguice.jms.excception;

import javax.jms.Message;

public interface MessageQueueExceptionHandler extends MessageQueueBaseExceptionHandler {
	default boolean onException(Message jmsMessage, String message, Exception exception) {
        return onException(message, exception);
    }
	boolean onException(String message, Exception exception);

}
