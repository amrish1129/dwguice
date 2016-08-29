package in.hopscotch.dwguice.jms;

import java.util.Optional;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.hopscotch.dwguice.MessageQueueConfig;
import in.hopscotch.dwguice.MessageQueuePoolConfig;
import io.dropwizard.setup.Environment;

public class ActiveMQManager implements JMSConfigManager {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private ActiveMQConnectionFactory connectionFactory;
	private PooledConnectionFactory poolFactory;
	private ObjectMapper objectMapper;
	private Environment env;
	private long shutdownWaitInSeconds;
	private Optional<Integer> defaultTimeToLiveInSeconds;
	public static final ThreadLocal<String> correlationID = new ThreadLocal<>();

	public void init(MessageQueueConfig config, Environment env) {
		final String brokerUrl = config.getBrokerUrl();
		final int configuredTTL = config.getTimeToLiveInSeconds();
		final Optional<String> username = Optional.ofNullable(config.getBrokerUsername());
		final Optional<String> password = Optional.ofNullable(config.getBrokerPassword());

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

	public void configurePoolFactory(MessageQueuePoolConfig poolConfig) {
		if (poolConfig == null) return;
		if (poolConfig.maxConnections != null) {
			poolFactory.setMaxConnections(poolConfig.maxConnections);
		}
		if (poolConfig.maxConnections != null) {
			poolFactory.setMaxConnections(poolConfig.maxConnections);
        }

        if (poolConfig.maximumActiveSessionPerConnection != null) {
        	poolFactory.setMaximumActiveSessionPerConnection(poolConfig.maximumActiveSessionPerConnection);
        }

        if (poolConfig.blockIfSessionPoolIsFull != null) {
        	poolFactory.setBlockIfSessionPoolIsFull(poolConfig.blockIfSessionPoolIsFull);
        }

        if (poolConfig.idleTimeoutMills != null) {
        	poolFactory.setIdleTimeout(poolConfig.idleTimeoutMills);
        }

        if (poolConfig.expiryTimeoutMills != null) {
        	poolFactory.setExpiryTimeout(poolConfig.expiryTimeoutMills);
        }

        if (poolConfig.createConnectionOnStartup != null) {
        	poolFactory.setCreateConnectionOnStartup(poolConfig.createConnectionOnStartup);
        }

        if (poolConfig.timeBetweenExpirationCheckMillis != null) {
        	poolFactory.setTimeBetweenExpirationCheckMillis(poolConfig.timeBetweenExpirationCheckMillis);
        }
	}

	@Override
	public void start() throws Exception {
		logger.info("Starting ActiveMQ Client");
		poolFactory.start();
	}

	@Override
	public void stop() throws Exception {
		logger.info("Stopping ActiveMQ Client");
		poolFactory.stop();
	}

}
