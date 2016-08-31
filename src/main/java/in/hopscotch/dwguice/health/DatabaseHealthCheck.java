package in.hopscotch.dwguice.health;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import com.codahale.metrics.health.HealthCheck;

public class DatabaseHealthCheck extends HealthCheck {
	private final DBI dbi;
	private final String validationQuery;
	public DatabaseHealthCheck(DBI dbi, String validationQuery) {
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
