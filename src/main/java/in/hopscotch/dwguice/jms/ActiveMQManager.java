package in.hopscotch.dwguice.jms;

import java.util.Optional;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.hopscotch.dwguice.MessageQueueConfig;
import in.hopscotch.dwguice.MessageQueuePoolConfig;
import in.hopscotch.dwguice.api.jms.JMSConfigManager;
import in.hopscotch.dwguice.api.jms.MessageSender;
import in.hopscotch.dwguice.api.jms.MessageReceiver;
import io.dropwizard.setup.Environment;

/**
 * 
 * @author amrish
 *
 */
public class ActiveMQManager implements JMSConfigManager {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private ActiveMQConnectionFactory connectionFactory;
	private PooledConnectionFactory poolFactory;
	private ObjectMapper objectMapper;
	//private Environment env;
	private long shutdownWaitInSeconds;
	private Optional<Integer> defaultTimeToLiveInSeconds;
	public static final ThreadLocal<String> correlationID = new ThreadLocal<>();

	public void init(MessageQueueConfig config, Environment env) {
		final String brokerUrl = config.getBrokerUrl();
		final int configuredTTL = config.getTimeToLiveInSeconds();
		defaultTimeToLiveInSeconds = Optional.ofNullable(configuredTTL > 0 ? configuredTTL : null);
		final Optional<String> username = Optional.ofNullable(config.getBrokerUsername());
		final Optional<String> password = Optional.ofNullable(config.getBrokerPassword());
		shutdownWaitInSeconds = config.getShutdownWaitInSeconds();
		//this.env = env;
		objectMapper = env.getObjectMapper();

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
	
	public MessageSender createSender(String destination, boolean persistent) {
        return createSender(destination, persistent, defaultTimeToLiveInSeconds);
    }

    public MessageSender createSender(String destination, boolean persistent, Optional<Integer> timeToLiveInSeconds) {
        return new MessageSenderImpl.Builder().setConnectionFactory(connectionFactory)
        		.setDestination(destination)
        		.setPersistent(persistent)
        		.setTimeToLiveInSeconds(timeToLiveInSeconds).build();
    }
    
    public <T> ActiveMQReceiverHandler<T> registerReceiver(String destination, MessageReceiver<T> receiver, Class<? extends T> clazz,
            final boolean ackMessageOnException) {
    	ActiveMQReceiverHandler<T> handler = new ActiveMQReceiverHandler.Builder<T>()
    			.setDestination(destination)
    			.setConnectionFactory(connectionFactory)
    			.setReceiver(receiver)
    			.setReceiverType(clazz)
    			.setObjectMapper(objectMapper)
    			.setExceptionHandler((message, exception) -> {
					if (ackMessageOnException) {
						logger.error("Error processing received message - acknowledging it anyway", exception);
						return true;
					} else {
						logger.error("Error processing received message - NOT acknowledging it", exception);
						return false;
					}
				})
    			.setShutdownWaitInSeconds(shutdownWaitInSeconds)
    			.build();
    	return handler;
		//internalRegisterReceiver(destination, handler);
	}


//	private <T> void internalRegisterReceiver(String destination, ActiveMQReceiverHandler<T> handler) {
//		// TODO Auto-generated method stub
//		env.lifecycle().manage(handler);
//		env.healthChecks().register("ActiveMQ receiver for " + destination, handler.getHealthCheck());
//	}

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
