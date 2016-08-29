package in.hopscotch.dwguice.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResourceCleaner {
	private final static Logger log = LoggerFactory.getLogger(ResourceCleaner.class);

    public interface RunnableThrowsAll {
        void run() throws Exception;
    }

    // swallows exceptions
    public static void clean( RunnableThrowsAll runnable ) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.debug("Suppressed exception", e);
        }
    }

}
