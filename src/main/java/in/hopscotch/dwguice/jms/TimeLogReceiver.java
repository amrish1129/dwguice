package in.hopscotch.dwguice.jms;

import in.hopscotch.dwguice.api.jms.MessageReceiver;
import in.hopscotch.dwguice.core.TimeLogRequest;

public class TimeLogReceiver implements MessageReceiver<TimeLogRequest> {

	@Override
	public void receive(TimeLogRequest message) {
		// TODO Auto-generated method stub
		System.out.println();
	}

}
