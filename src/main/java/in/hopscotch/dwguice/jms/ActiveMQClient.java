package in.hopscotch.dwguice.jms;

import java.util.Optional;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;

import in.hopscotch.dwguice.MessageQueueConfig;
import in.hopscotch.dwguice.health.MessageQueueHealthCheck;

@Singleton
public class ActiveMQClient {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private ActiveMQConnectionFactory connectionFactory;
	private PooledConnectionFactory poolFactory;
	private ObjectMapper objectMapper;
	private long shutdownWaitInSeconds;
	private Optional<Integer> defaultTimeToLiveInSeconds;
	public static final ThreadLocal<String> correlationID = new ThreadLocal<>();
	
	public ActiveMQClient(MessageQueueConfig config, ObjectMapper objectMapper) {
		init(config, objectMapper);
	}
	
	public ActiveMQConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}
	
	private void init(MessageQueueConfig config, ObjectMapper objectMapper) {
		final String brokerUrl = config.getBrokerUrl();
		final int configuredTTL = config.getTimeToLiveInSeconds();
		defaultTimeToLiveInSeconds = Optional.ofNullable(configuredTTL > 0 ? configuredTTL : null);
		final Optional<String> username = Optional.ofNullable(config.getBrokerUsername());
		final Optional<String> password = Optional.ofNullable(config.getBrokerPassword());
		shutdownWaitInSeconds = config.getShutdownWaitInSeconds();
		//this.env = env;
		objectMapper = this.objectMapper;

		logger.info("Setting up ActiveMQ with Broker URL:" + brokerUrl);
		logger.debug("ActiveMQ Config details:" + config);

		connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		if (username.isPresent() && password.isPresent()) {
			connectionFactory.setUserName(username.get());
			connectionFactory.setPassword(password.get());
		}
		
		poolFactory = new PooledConnectionFactory();
		poolFactory.setConnectionFactory(connectionFactory);
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public long getShutdownWaitInSeconds() {
		return shutdownWaitInSeconds;
	}

	public Optional<Integer> getDefaultTimeToLiveInSeconds() {
		return defaultTimeToLiveInSeconds;
	}

	public PooledConnectionFactory getPoolFactory() {
		return poolFactory;
	}
	
	public MessageQueueHealthCheck getMessageQueueHealthCheck() {
		return new MessageQueueHealthCheck(connectionFactory, 2000);
	}
	
}
