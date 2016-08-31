package in.hopscotch.dwguice.jms;

public enum MessageDestination {
	
	TIMELOG("queue:timelog"),
	SENDSMS("queue:sendsms");
	
	private String name;
	
	MessageDestination(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
