package in.hopscotch.dwguice.di;

import org.skife.jdbi.v2.DBI;

import com.google.inject.Inject;
import com.google.inject.Provider;

import in.hopscotch.dwguice.db.TimeLogDao;

public class TimeLogDaoProvider implements Provider<TimeLogDao>{
	
	private DBI jdbi;
	@Inject
	public TimeLogDaoProvider(DBI jdbi) {
		this.jdbi = jdbi;
	}
	
	@Override
	public TimeLogDao get() {
		return jdbi.onDemand(TimeLogDao.class);
	}

}
