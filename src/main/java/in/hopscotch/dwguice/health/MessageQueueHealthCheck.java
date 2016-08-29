package in.hopscotch.dwguice.health;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import com.codahale.metrics.health.HealthCheck;

public class MessageQueueHealthCheck extends HealthCheck {
	private ConnectionFactory connectionFactory;
	private long millisecondsToWait;

	public MessageQueueHealthCheck(ConnectionFactory connectionFactory, long millisecondsToWait) {
		this.connectionFactory = connectionFactory;
		this.millisecondsToWait = millisecondsToWait;
	}

	@Override
	protected Result check() throws Exception {
		/*
		 * Connection connection = connectionFactory.createConnection();
		 * connection.start(); try { Session session =
		 * connection.createSession(false, Session.AUTO_ACKNOWLEDGE); try {
		 * TemporaryQueue tempQueue = session.createTemporaryQueue(); try {
		 * MessageProducer producer = session.createProducer(tempQueue); } catch
		 * (Exception e) { handleException(()-> tempQueue.delete()); } } catch
		 * (Exception e) { handleException(() -> session.close()); return
		 * Result.unhealthy(e.getMessage()); } }catch(Exception e) {
		 * handleException(() -> connection.close()); return
		 * Result.unhealthy(e.getMessage()); }
		 */
		Connection connection = connectionFactory.createConnection();
		connection.start();
		try {
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			try {
				TemporaryQueue tempQueue = session.createTemporaryQueue();

				try {
					MessageProducer producer = session.createProducer(tempQueue);
					try {
						// Publish a test message
						producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
						final String messageText = "Test message-" + System.currentTimeMillis();
						producer.send(tempQueue, session.createTextMessage(messageText));

						MessageConsumer consumer = session.createConsumer(tempQueue);
						try {
							// Wait for our testMessage
							TextMessage receivedMessage = (TextMessage) consumer.receive(millisecondsToWait);

							// Make sure we received the correct message
							if (receivedMessage != null && messageText.equals(receivedMessage.getText())) {
								return Result.healthy();
							} else {
								return Result.unhealthy("Did not receive testMessage via tempQueue in "
										+ millisecondsToWait + " milliseconds");
							}
						} finally {
							handleException(() -> consumer.close());
						}
					} finally {
						handleException(() -> producer.close());
					}
				} finally {
					handleException(() -> tempQueue.delete());
				}
			} finally {
				handleException(() -> session.close());
			}
		} finally {
			handleException(() -> connection.close());
		}
	}

	protected interface CleanResource {
		void cleanup() throws Exception;
	}

	protected void handleException(CleanResource cr) {
		try {
			cr.cleanup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
