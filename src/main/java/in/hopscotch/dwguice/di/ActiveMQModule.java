package in.hopscotch.dwguice.di;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import in.hopscotch.dwguice.api.jms.MessageSender;
import in.hopscotch.dwguice.jms.ActiveMQClient;
import in.hopscotch.dwguice.jms.ActiveMQManager;

public class ActiveMQModule extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(ActiveMQClient.class).toProvider(ActiveMQClientProvider.class).in(Singleton.class);
		bind(ActiveMQManager.class).toProvider(ActiveMQManagerProvider.class).in(Singleton.class);
		bind(MessageSender.class).toProvider(MessageSenderProvider.class);
	}

}
