package in.hopscotch.dwguice.api.jms;

import in.hopscotch.dwguice.MessageQueueConfig;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;

/**
 * 
 * @author amrish
 *
 */
public interface JMSConfigManager extends Managed {
	void init(MessageQueueConfig config, Environment env);
}
