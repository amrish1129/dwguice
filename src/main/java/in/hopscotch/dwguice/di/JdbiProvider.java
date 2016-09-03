package in.hopscotch.dwguice.di;

import org.skife.jdbi.v2.DBI;

import com.google.inject.Inject;
import com.google.inject.Provider;

import in.hopscotch.dwguice.MainConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

public class JdbiProvider implements Provider<DBI>{
	
	private MainConfiguration configuration;
	private Environment environment;
	@Inject
	public JdbiProvider(MainConfiguration configuration, Environment environment) {
		this.configuration = configuration;
		this.environment = environment;
	}
	
	@Override
	public DBI get() {
		final DBIFactory factory = new DBIFactory();
		DataSourceFactory dsFactory = configuration.getDataSourceFactory();
		final DBI jdbi = factory.build(environment, dsFactory, "mysql");
		return jdbi;
	}

}
