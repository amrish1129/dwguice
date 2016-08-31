package in.hopscotch.dwguice.api.jms;

import javax.jms.Message;

/**
 * 
 * @author amrish
 *
 */
public interface MessageBaseExceptionHandler {
	boolean onException(Message jmsMessage, String message, Exception exception);
}
