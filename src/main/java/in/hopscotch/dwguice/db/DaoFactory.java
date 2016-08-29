package in.hopscotch.dwguice.db;

import org.skife.jdbi.v2.DBI;

public class DaoFactory {
	
	private static DaoFactory daoFactory = new DaoFactory();
	private DBI dbi;
	private String validationQuery;

	public static DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public static void setDaoFactory(DaoFactory daoFactory) {
		DaoFactory.daoFactory = daoFactory;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public DBI getDbi() {
		return dbi;
	}
	
	private DaoFactory() {
		
	}
	
	public OrderTimeLogDao getOrderTimeLogDao()  {
		return dbi.onDemand(OrderTimeLogDao.class);
	}

	public void setDbi(DBI dbi) throws Exception {
		if( this.dbi != null) throw new Exception();
		this.dbi = dbi;
	}
	
	public static DaoFactory getInstance() {
		return daoFactory;
	}
	
}
