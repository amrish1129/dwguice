package in.hopscotch.dwguice.health;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DatabaseHealthCheck extends HealthCheck {
	private final DBI dbi;
	private final String validationQuery;
	
	@Inject
	public DatabaseHealthCheck(DBI dbi, @Named("validationQuery")String validationQuery) {
		this.dbi = dbi;
		this.validationQuery = validationQuery;
	}
	
	@Override
	protected Result check() throws Exception {
		try (Handle h = dbi.open()) {
			h.execute(validationQuery);
			return Result.healthy();
		} catch (Exception e) {
			return Result.unhealthy(e);
		}
	}

}
