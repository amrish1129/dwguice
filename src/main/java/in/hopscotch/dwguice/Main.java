package in.hopscotch.dwguice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import in.hopscotch.dwguice.auth.SecurityFilter;
import in.hopscotch.dwguice.auth.UserAuthenticator;
import in.hopscotch.dwguice.di.ActiveMQModule;
import in.hopscotch.dwguice.di.ConfigModule;
import in.hopscotch.dwguice.di.DBModule;
import in.hopscotch.dwguice.health.DatabaseHealthCheck;
import in.hopscotch.dwguice.jms.ActiveMQClient;
import in.hopscotch.dwguice.jms.ActiveMQManager;
import in.hopscotch.dwguice.jms.ActiveMQReceiverHandler;
import in.hopscotch.dwguice.jms.MessageDestination;
import in.hopscotch.dwguice.resource.OrderTrackingResourceImpl;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.setup.Environment;

/**
 * This is application's entry point class
 * 
 * @author amrish
 *
 */
public class Main extends Application<MainConfiguration> {

	private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

	/**
	 * The initialize method is tasked with bootstrapping, possibly loading
	 * additional components and generally preparing the runtime environment of
	 * the application.
	 */
	/*
	 * @Override public void initialize(Bootstrap<MainConfiguration> bootstrap)
	 * { // TODO Auto-generated method stub /guiceBundle =
	 * GuiceBundle.<MainConfiguration>newBuilder() .addModule(new MainModule())
	 * .setConfigClass(MainConfiguration.class) .build();
	 * bootstrap.addBundle(guiceBundle); //bootstrap.a }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.dropwizard.Application#run(io.dropwizard.Configuration,
	 * io.dropwizard.setup.Environment)
	 */
	@Override
	public void run(MainConfiguration configuration, Environment environment) throws Exception {
		LOGGER.info("Method App#run() called");
		// System.out.println( "Hello world, by Dropwizard!" );
		// Add all the implemented resource to our Dropwizard application's
		// environment via the
		// JerseyEnvironment#register() method.

		Injector injector = Guice.createInjector(new ConfigModule(configuration, environment), new ActiveMQModule(),
				new DBModule()
				//new MainModule()
				);

		environment.jersey().register(injector.getInstance(OrderTrackingResourceImpl.class));
		// environment.jersey().register(new
		// LoggingFeature(java.util.logging.Logger.getLogger(OrderTrackingResourceImpl.class.getName())));

		environment.lifecycle().manage(injector.getInstance(ActiveMQManager.class));

		ActiveMQReceiverHandler<String> handler = injector.getInstance(ActiveMQManager.class).registerReceiver(
				MessageDestination.TIMELOG.getName(), (str) -> System.out.println(str), String.class, true);

		environment.lifecycle().manage(handler);
		environment.healthChecks().register("ActiveMQ receiver for " + MessageDestination.TIMELOG.getName(),
				handler.getHealthCheck());

		environment.healthChecks().register("messageQueueHealthCheck",
				injector.getInstance(ActiveMQClient.class).getMessageQueueHealthCheck());
		environment.healthChecks().register("mysqldb", injector.getInstance(DatabaseHealthCheck.class));
		
		UserAuthenticator authenticator = new UserAuthenticator();
		SecurityFilter filter = new SecurityFilter(authenticator);
		
		environment.jersey().register(new AuthDynamicFeature(filter));

	}

	public static void main(String[] args) throws Exception {
		new Main().run(args);
	}

}
