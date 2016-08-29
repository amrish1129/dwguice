package in.hopscotch.dwguice.jms;

import javax.jms.Message;
import javax.jms.Session;

public interface MessageQueueSender {
	void sendJson(String json);
    void send(Object object);
    void send(JMSFunction<Session, Message> messageCreator);
}
