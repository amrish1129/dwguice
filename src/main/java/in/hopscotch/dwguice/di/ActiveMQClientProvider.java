package in.hopscotch.dwguice.di;

import com.google.inject.Inject;
import com.google.inject.Provider;

import in.hopscotch.dwguice.MainConfiguration;
import in.hopscotch.dwguice.jms.ActiveMQClient;
import io.dropwizard.setup.Environment;

public class ActiveMQClientProvider implements Provider<ActiveMQClient>{
	private MainConfiguration config;
	private Environment environment;
	
	@Inject
	ActiveMQClientProvider(MainConfiguration config, Environment environment) {
		this.config = config;
		this.environment = environment;
	}

	@Override
	public ActiveMQClient get() {
		return new ActiveMQClient(config.getMessageQueueConfig(), environment.getObjectMapper());
	}

}
