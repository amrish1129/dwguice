package in.hopscotch.dwguice.di;

import org.skife.jdbi.v2.DBI;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import in.hopscotch.dwguice.MainConfiguration;
import in.hopscotch.dwguice.db.TimeLogDao;

public class DBModule extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(DBI.class).toProvider(JdbiProvider.class).in(Singleton.class);
		bind(TimeLogDao.class).toProvider(TimeLogDaoProvider.class).in(Singleton.class);
	}
	
	@Provides
	@Named("validationQuery")
	public String provideValidationQuery(MainConfiguration config) {
		System.out.println("provideValidationQuery");
		return config.getDataSourceFactory().getValidationQuery();
	}

}
