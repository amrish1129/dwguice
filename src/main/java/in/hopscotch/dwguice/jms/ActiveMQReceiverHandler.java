package in.hopscotch.dwguice.jms;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.jms.pool.PooledMessageConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.hopscotch.dwguice.api.jms.MessageBaseExceptionHandler;
import in.hopscotch.dwguice.api.jms.MessageExceptionHandler;
import in.hopscotch.dwguice.api.jms.MessageReceiver;

public class ActiveMQReceiverHandler<T> extends MessageReceiverHandlerImpl {
	private final Class<? extends T> receiverType;
	private final MessageReceiver<T> receiver;
	private final ObjectMapper objectMapper;
	static final Field pooledMessageConsumerDelegateField;
	private final MessageBaseExceptionHandler exceptionHandler;

	static {
		try {
			pooledMessageConsumerDelegateField = PooledMessageConsumer.class.getDeclaredField("delegate");
			pooledMessageConsumerDelegateField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	private ActiveMQReceiverHandler(String destination, ConnectionFactory connectionFactory, MessageReceiver<T> receiver,
			Class<? extends T> receiverType, ObjectMapper objectMapper,
			MessageBaseExceptionHandler exceptionHandler, long shutdownWaitInSeconds) {
		super(destination, connectionFactory, exceptionHandler,
				shutdownWaitInSeconds);
		this.receiverType = receiverType;
		this.receiver = receiver;
		this.objectMapper = objectMapper;
		this.exceptionHandler = exceptionHandler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(MessageConsumer rawMessageConsumer, Message message) throws Exception {
		String json = null;
		ActiveMQMessageConsumer messageConsumer = convertToActiveMQMessageConsumer(rawMessageConsumer);
		try {
			if (message instanceof TextMessage) {
				json = ((TextMessage) message).getText();
				if (receiverType.equals(String.class)) {
					receiver.receive((T)json);
				} else {
					T object = fromJson(json);
			        receiver.receive(object);
				}
			} else if (message instanceof ActiveMQMapMessage) {
				ActiveMQMapMessage mm = (ActiveMQMapMessage) message;
				if (receiverType.equals(Map.class)) {
					receiver.receive((T) mm.getContentMap());
				} else {
					throw new Exception();
					
				}
			} else {
				throw new Exception();
			}
			message.acknowledge();
		} catch (Exception e) {
			if (exceptionHandler.onException(message, json, e)) {
				try {
                    message.acknowledge();
                } catch (JMSException x) {
                    throw new RuntimeException(x);
                }
			} else {
                try {
                    messageConsumer.rollback();
                } catch (JMSException e1) {
                    throw new RuntimeException("Error rollbacking failed message", e1);
                }
            }
		}
		
	}

	private T fromJson(String json) {
		try {
            return (T)objectMapper.readValue(json, receiverType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	private ActiveMQMessageConsumer convertToActiveMQMessageConsumer(MessageConsumer rawMessageConsumer) {
		if (rawMessageConsumer instanceof ActiveMQMessageConsumer) {
			return (ActiveMQMessageConsumer) rawMessageConsumer;
		} else if (rawMessageConsumer instanceof PooledMessageConsumer) {
			try {
				return (ActiveMQMessageConsumer) pooledMessageConsumerDelegateField.get(rawMessageConsumer);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(
						"Error extracting ActiveMQMessageConsumer from " + rawMessageConsumer.getClass(), e);
			}
		} else {
			throw new RuntimeException("Unable to convert messageConsumer '" + rawMessageConsumer.getClass()
					+ "' to ActiveMQMessageConsumer");
		}
	}
	
	public static class Builder<T> {
		private Class<? extends T> receiverType;
		private MessageReceiver<T> receiver;
		private ObjectMapper objectMapper;
		static Field pooledMessageConsumerDelegateField;
		private String destination; 
		private ConnectionFactory connectionFactory;
		private MessageExceptionHandler exceptionHandler;
		private long shutdownWaitInSeconds;
		
		
		public Builder<T> setReceiverType(Class<? extends T> receiverType) {
			this.receiverType = receiverType;
			return this;
		}
		public Builder<T> setReceiver(MessageReceiver<T> receiver) {
			this.receiver = receiver;
			return this;
		}
		public Builder<T> setObjectMapper(ObjectMapper objectMapper) {
			this.objectMapper = objectMapper;
			return this;
		}
		public Builder<T> setExceptionHandler(MessageExceptionHandler exceptionHandler) {
			this.exceptionHandler = exceptionHandler;
			return this;
			
		}
		
		public Builder<T> setDestination(String destination) {
			this.destination = destination;
			return this;
		}
		public Builder<T> setConnectionFactory(ConnectionFactory connectionFactory) {
			this.connectionFactory = connectionFactory;
			return this;
		}
		public Builder<T> setShutdownWaitInSeconds(long shutdownWaitInSeconds) {
			this.shutdownWaitInSeconds = shutdownWaitInSeconds;
			return this;
		}
		public ActiveMQReceiverHandler<T> build() {
			return new ActiveMQReceiverHandler<T>(destination, 
					connectionFactory, receiver, receiverType, objectMapper, exceptionHandler, shutdownWaitInSeconds);
		}
		
	}
}
