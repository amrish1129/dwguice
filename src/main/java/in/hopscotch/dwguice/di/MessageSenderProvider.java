package in.hopscotch.dwguice.di;

import com.google.inject.Inject;
import com.google.inject.Provider;

import in.hopscotch.dwguice.api.jms.MessageSender;
import in.hopscotch.dwguice.jms.ActiveMQManager;
import in.hopscotch.dwguice.jms.MessageDestination;

public class MessageSenderProvider implements Provider<MessageSender>{
	ActiveMQManager manager;
	
	@Inject
	public MessageSenderProvider(ActiveMQManager manager) {
		this.manager = manager;
	}

	@Override
	public MessageSender get() {
		return manager.createSender(MessageDestination.TIMELOG.getName(), true);
	}

}
