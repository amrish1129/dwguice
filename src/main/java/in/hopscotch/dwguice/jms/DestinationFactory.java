package in.hopscotch.dwguice.jms;

import javax.jms.Destination;
import javax.jms.Session;

public interface DestinationFactory {
	Destination create(Session session, String name);
}
