package in.hopscotch.dwguice.api.jms;

import javax.jms.Destination;
import javax.jms.Session;
/**
 * 
 * @author amrish
 *
 */
public interface DestinationFactory {
	Destination create(Session session, String name);
}
