package in.hopscotch.dwguice;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import in.hopscotch.dwguice.db.DaoFactory;
import in.hopscotch.dwguice.db.OrderTimeLogDao;
import in.hopscotch.dwguice.health.DatabaseHealthCheck;
import in.hopscotch.dwguice.health.MessageQueueHealthCheck;
import in.hopscotch.dwguice.jms.MessageQueueClient;
import in.hopscotch.dwguice.representations.OrderTimeLog;
import in.hopscotch.dwguice.resource.OrderTrackingResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
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
	/*@Override
	public void initialize(Bootstrap<MainConfiguration> bootstrap) {
		// TODO Auto-generated method stub
		super.initialize(bootstrap);
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
		environment.jersey().register(OrderTrackingResource.class);
		
	    MessageQueueClient messageQueue = configuration.getMessageQueueFactory().build(environment);
	    
	    environment.healthChecks().register("databse", new DatabaseHealthCheck());
	    environment.healthChecks().register("jms", new MessageQueueHealthCheck());
	    
	    final DBIFactory factory = new DBIFactory();
	    DataSourceFactory dsFactory = configuration.getDataSourceFactory();
	    final DBI jdbi = factory.build(environment, dsFactory, "mysql");
	    
	    //final OrderTimeLogDao otlDap = dbi.onDemand(OrderTimeLogDao.class);
	    DaoFactory daoFactory = DaoFactory.getInstance();
	    daoFactory.setDbi(jdbi);
	    daoFactory.setValidationQuery(dsFactory.getValidationQuery());
	    

	}
	
	public static void main(String[] args) throws Exception {
		new Main().run(args);
	}

}
