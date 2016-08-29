package in.hopscotch.dwguice.jms;

import in.hopscotch.dwguice.MessageQueueConfig;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;

public interface JMSConfigManager extends Managed {
	void init(MessageQueueConfig config, Environment env);
}
