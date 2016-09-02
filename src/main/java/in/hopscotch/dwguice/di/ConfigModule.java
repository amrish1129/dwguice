package in.hopscotch.dwguice.di;

import com.google.inject.AbstractModule;

import in.hopscotch.dwguice.MainConfiguration;
import io.dropwizard.setup.Environment;

public class ConfigModule extends AbstractModule {
	private MainConfiguration config;
	private Environment environment;
	public ConfigModule(MainConfiguration config, Environment environment) {
		this.config = config;
		this.environment = environment;
	}

	@Override
	protected void configure() {
		bind(MainConfiguration.class).toInstance(config);
		bind(Environment.class).toInstance(environment);
	}

}
