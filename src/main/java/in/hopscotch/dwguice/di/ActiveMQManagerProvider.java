package in.hopscotch.dwguice.di;

import com.google.inject.Inject;
import com.google.inject.Provider;

import in.hopscotch.dwguice.jms.ActiveMQClient;
import in.hopscotch.dwguice.jms.ActiveMQManager;

public class ActiveMQManagerProvider implements Provider<ActiveMQManager>{
	ActiveMQClient client;
	@Inject
	ActiveMQManagerProvider(ActiveMQClient client) {
		this.client = client;
	}

	@Override
	public ActiveMQManager get() {
		// TODO Auto-generated method stub
		return new ActiveMQManager(client);
	}

}
