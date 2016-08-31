package in.hopscotch.dwguice.jms;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck;

import in.hopscotch.dwguice.api.jms.DestinationFactory;
import in.hopscotch.dwguice.api.jms.MessageBaseExceptionHandler;
import io.dropwizard.lifecycle.Managed;

public abstract class MessageReceiverHandler implements Managed, Runnable {
	
	private static final long SLEEP_TIME_MILLS = 10000;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String destination;
    private final ConnectionFactory connectionFactory;
//	  private final Class<? extends T> receiverType;
//    private final MessageReceiver<T> receiver;
//    private final ObjectMapper objectMapper;
    private final Thread thread;
    private AtomicBoolean shouldStop = new AtomicBoolean(false);
    private AtomicBoolean isReceiving = new AtomicBoolean(false);
    private final MessageBaseExceptionHandler exceptionHandler;
    protected final DestinationFactory destinationFactory = new DestinationFactoryImpl();
    protected final long shutdownWaitInSeconds;
    
    protected int errorsInARowCount = 0;
    
    public MessageReceiverHandler(
    		String destination,
            ConnectionFactory connectionFactory,
            MessageBaseExceptionHandler exceptionHandler,
            long shutdownWaitInSeconds) {
    	
    	this.destination = destination;
        this.connectionFactory = connectionFactory;
//        this.receiver = receiver;
//        this.receiverType = receiverType;
//        this.objectMapper = objectMapper;
        this.exceptionHandler = exceptionHandler;
        this.shutdownWaitInSeconds = shutdownWaitInSeconds;

        this.thread = new Thread(this, "Receiver "+destination);
    	
    }
    
    /*
    public MessageQueueReceiverHandler(
    		String destination,
            ConnectionFactory connectionFactory,
            MessageReceiver<T> receiver,
            Class<? extends T> receiverType,
            ObjectMapper objectMapper,
            MessageQueueExceptionHandler exceptionHandler,
            long shutdownWaitInSeconds) {
    	
        this(destination, connectionFactory, receiver, receiverType, objectMapper, (MessageQueueBaseExceptionHandler) exceptionHandler, shutdownWaitInSeconds);

    	
    }*/

	@Override
	public void run() {
		errorsInARowCount = 0;
		boolean verboseInitLogging = true;
		while (!shouldStop.get()) {
			log.info("Setting up receiver for " + destination);
			try {
				final Connection connection = connectionFactory.createConnection();
				try {
					connection.start();
					final Session session =  connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
					try {
						final Destination dest = destinationFactory.create(session, destination);
						final MessageConsumer rawMessageConsumer = session.createConsumer(dest);
						try {
							isReceiving.set(true);
							runReceiveLoop(rawMessageConsumer);
						} finally {
							isReceiving.set(false);
							ResourceCleaner.clean(() -> rawMessageConsumer.close());
						}
						
					} finally {
						ResourceCleaner.clean(() -> session.close());
					}
				} finally {
					ResourceCleaner.clean(()->connection.close());
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				errorsInARowCount++;
				boolean continuingErrorSituation = errorsInARowCount > 1;
				verboseInitLogging = true;
				if ( e instanceof javax.jms.IllegalStateException
                        && e.getMessage().equals("The Consumer is closed")
                        && !continuingErrorSituation) {
                    // This is the first error we see,
                    // and it is the "javax.jms.IllegalStateException: The Consumer is closed"-error
                    // log it as debug.
                    log.debug("Consumer is closed - will try to recover", e);
                    // In this situation we do not want to verbose log the following initialization
                    verboseInitLogging = false;
                } else {
                    log.error("Uncaught exception - will try to recover", e);
                }
				
				// Prevent using too much CPU when stuff does not work
                if (continuingErrorSituation) {
                    log.warn("Numbers of errors in a row {} - Going to sleep {} mills before retrying", errorsInARowCount, SLEEP_TIME_MILLS);
                    ResourceCleaner.clean(() -> Thread.sleep(SLEEP_TIME_MILLS));
                }
				
			}
		}
		log.debug("Message-checker-thread stopped");
	}

	private void runReceiveLoop(MessageConsumer rawMessageConsumer) throws JMSException, Exception {
		while(!shouldStop.get()) {
			Message message = rawMessageConsumer.receive();
			errorsInARowCount = 0;
			if (message != null) {
				processMessage(rawMessageConsumer, message);
			}
		}
	}

	protected abstract void processMessage(MessageConsumer rawMessageConsumer, Message message) throws Exception;

	@Override
	public void start() throws Exception {
		log.info("Starting receiver for " + destination);
        thread.start();
	}

	@Override
	public void stop() throws Exception {
		log.info("Stopping receiver for " + destination + " (Going to wait for max " + shutdownWaitInSeconds + " seconds)");
		if(thread.isAlive()) {
			shouldStop.set(true);
			final long start = System.currentTimeMillis();
			while (thread.isAlive()) {
                if (((System.currentTimeMillis() - start) / 1000) >= shutdownWaitInSeconds) {
                    log.warn("Giving up waiting for receiver-thread shutdown");
                    break;
                }
                log.debug("ReceiverThread is still alive..");
                Thread.sleep(200);
            }
		}
	}
	
	public HealthCheck getHealthCheck() {
        return new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                if (isReceiving.get()) {
                    return Result.healthy("Is receiving from " + destination);
                } else {
                    return Result.unhealthy("Is NOT receiving from " + destination);
                }
            }
        };
    }

}
