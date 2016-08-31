package in.hopscotch.dwguice.api.jms;

import javax.jms.Message;

/**
 * 
 * @author amrish
 *
 */
public interface MessageExceptionHandler extends MessageBaseExceptionHandler {
	default boolean onException(Message jmsMessage, String message, Exception exception) {
        return onException(message, exception);
    }
	boolean onException(String message, Exception exception);

}
