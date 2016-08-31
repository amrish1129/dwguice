package in.hopscotch.dwguice.api.jms;

import javax.jms.Message;
import javax.jms.Session;

/**
 * 
 * @author amrish
 *
 */
public interface MessageSender {
	void sendJson(String json);
    void send(Object object);
    void send(JMSFunction<Session, Message> messageCreator);
}
