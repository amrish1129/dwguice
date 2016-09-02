package in.hopscotch.dwguice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hubspot.dropwizard.guice.GuiceBundle;

import in.hopscotch.dwguice.di.ActiveMQModule;
import in.hopscotch.dwguice.di.ConfigModule;
import in.hopscotch.dwguice.health.DatabaseHealthCheck;
import in.hopscotch.dwguice.jms.ActiveMQClient;
import in.hopscotch.dwguice.jms.ActiveMQManager;
import in.hopscotch.dwguice.jms.ActiveMQReceiverHandler;
import in.hopscotch.dwguice.jms.MessageDestination;
import in.hopscotch.dwguice.resource.OrderTrackingResourceImpl;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * This is application's entry point class
 * 
 * @author amrish
 *
 */
public class Main extends Application<MainConfiguration> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(Main.class);
	private GuiceBundle<MainConfiguration> guiceBundle;

	/**
	 * The initialize method is tasked with bootstrapping, possibly loading
	 * additional components and generally preparing the runtime environment of
	 * the application.
	 */
	/*@Override
	public void initialize(Bootstrap<MainConfiguration> bootstrap) {
		// TODO Auto-generated method stub
		/guiceBundle = GuiceBundle.<MainConfiguration>newBuilder()
				.addModule(new MainModule())
				.setConfigClass(MainConfiguration.class)
				.build();
		bootstrap.addBundle(guiceBundle);
		//bootstrap.a
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.dropwizard.Application#run(io.dropwizard.Configuration,
	 * io.dropwizard.setup.Environment)
	 */
	@Override
	public void run(MainConfiguration configuration, Environment environment) throws Exception {
		LOGGER.info("Method App#run() called");
		//System.out.println( "Hello world, by Dropwizard!" );
		// Add all the implemented resource to our Dropwizard application's  environment via the
		// JerseyEnvironment#register() method.
		
	    //MessageQueueClient messageQueue = configuration.getMessageQueueFactory().build(environment);
		//ActiveMQManager manager = guiceBundle.getInjector().getInstance(ActiveMQManager.class);
		//guiceBundle.getInjector().
	    Injector injector = Guice.createInjector(new ConfigModule(configuration, environment),
	    		new ActiveMQModule(),
	    		new MainModule());
	    
	   // MessageSender sender = injector.getInstance(ActiveMQManager.class).createSender(MessageDestination.TIMELOG.getName(), true);
		
		environment.jersey().register(injector.getInstance(OrderTrackingResourceImpl.class));
		
		environment.lifecycle().manage(injector.getInstance(ActiveMQManager.class));
		
		ActiveMQReceiverHandler<String> handler = injector.getInstance(ActiveMQManager.class).registerReceiver(MessageDestination.TIMELOG.getName(), (str) -> System.out.println(str), String.class, true);
	   
		environment.lifecycle().manage(handler);
		environment.healthChecks().register("ActiveMQ receiver for " + MessageDestination.TIMELOG.getName(), handler.getHealthCheck());
		//environment.healthChecks().register("jms", new MessageQueueHealthCheck());
	    
	   /* final DBIFactory factory = new DBIFactory();
	    DataSourceFactory dsFactory = configuration.getDataSourceFactory();
	    final DBI jdbi = factory.build(environment, dsFactory, "mysql");
	    
	    //final OrderTimeLogDao otlDap = dbi.onDemand(OrderTimeLogDao.class);
	    DaoFactory daoFactory = DaoFactory.getInstance();
	    daoFactory.setDbi(jdbi);
	    daoFactory.setValidationQuery(dsFactory.getValidationQuery());
	    environment.healthChecks().register("database", new DatabaseHealthCheck(jdbi,dsFactory.getValidationQuery() ));*/
		environment.healthChecks().register("messageQueueHealthCheck", injector.getInstance(ActiveMQClient.class).getMessageQueueHealthCheck());
		environment.healthChecks().register("mysqldb", injector.getInstance(DatabaseHealthCheck.class));
	    
	    //Init Active MQ
	    //initMessageQueue(configuration, environment);

	}
	

	
	public static void main(String[] args) throws Exception {
		new Main().run(args);
	}

}
