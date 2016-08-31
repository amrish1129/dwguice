package in.hopscotch.dwguice.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import in.hopscotch.dwguice.api.jms.DestinationFactory;

public class DestinationFactoryImpl implements DestinationFactory {

	@Override
	public Destination create(Session session, String name) {
		try {
            if (name.startsWith("queue:")) {
                return session.createQueue(name.substring(6));
            } else if (name.startsWith("topic:")) {
                return session.createTopic(name.substring(6));
            } else {
                return session.createQueue(name);
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
	}

}
