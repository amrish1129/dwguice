package in.hopscotch.dwguice;

import org.skife.jdbi.v2.DBI;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

public class MainModule extends AbstractModule {
	
	/*@Inject
	private MainConfiguration config;
	@Inject
	private Environment environment;*/

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		/*bind(JMSConfigManager.class)
		.annotatedWith(ActiveMQ.class)
		.toProvider(providerType)*/
		//ActiveMQManager manager = new ActiveMQManager();
		//requestInjection(manager);
				
	}

	@Provides
	@Singleton
	public DBI provideJdbi(MainConfiguration config, Environment environment) {
		System.out.println("provideJdbi");
		final DBIFactory factory = new DBIFactory();
		DataSourceFactory dsFactory = config.getDataSourceFactory();
		final DBI jdbi = factory.build(environment, dsFactory, "mysql");
		return jdbi;
	}

	@Provides
	@Named("validationQuery")
	public String provideValidationQuery(MainConfiguration config) {
		System.out.println("provideValidationQuery");
		return config.getDataSourceFactory().getValidationQuery();
	}

	/*@Provides
	@Singleton
	public ActiveMQClient provideActiveMQClient(MainConfiguration config, Environment environment) {
		System.out.println("provideActiveMQClient");
		System.out.println("Config:::" +config.toString());
		System.out.println("Environment:::" +environment.toString());
		ActiveMQClient client = new ActiveMQClient(config.getMessageQueueConfig(), environment.getObjectMapper());
		System.out.println("Client::" + client);
		return client;
	}
	
	@Provides
	@Singleton
	public ActiveMQManager provideActiveMQManager(MainConfiguration config, Environment environment) {
		ActiveMQClient client = provideActiveMQClient(config, environment );
		System.out.println("Client::" + client);

		ActiveMQManager manager = new ActiveMQManager(client); 
		System.out.println("Manager::" + manager);

		return manager;
	}
	
	@Provides
	@Named("smsMessageSender")
	public MessageSender provideSMSMessageSender(MainConfiguration config, Environment environment) {
		ActiveMQManager manager = provideActiveMQManager(config, environment); 
		return manager.createSender(MessageDestination.SENDSMS.getName(), true);
	}
	
	@Provides
	@Named("timeLogMessageSender")
	public MessageSender provideTimeLogMessageSender(MainConfiguration config, Environment environment) {
		ActiveMQManager manager = provideActiveMQManager(config, environment); 
		return manager.createSender(MessageDestination.TIMELOG.getName(), true);
	}
	
	@Provides
	@Named("timeLogMessageHandler")
	public ActiveMQReceiverHandler<String> provideMessageReceiver(MainConfiguration config, Environment environment) {
		ActiveMQManager manager = provideActiveMQManager(config, environment); 
		System.out.println("Manager::" + manager);

		ActiveMQReceiverHandler<String> handler = manager.registerReceiver(MessageDestination.TIMELOG.getName(), (str) -> System.out.println(str), String.class, false);
		environment.lifecycle().manage(handler);
		environment.healthChecks().register("ActiveMQ receiver for " + MessageDestination.TIMELOG.getName(), handler.getHealthCheck());
		return handler;
	
	}*/

}
