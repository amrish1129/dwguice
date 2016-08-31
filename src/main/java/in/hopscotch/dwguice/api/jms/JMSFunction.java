package in.hopscotch.dwguice.api.jms;

import javax.jms.JMSException;

/**
 * 
 * @author amrish
 *
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface JMSFunction<T, R> {
    R apply(T t) throws JMSException;
}
