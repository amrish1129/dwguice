package in.hopscotch.dwguice.jms;

import javax.jms.JMSException;

@FunctionalInterface
public interface JMSFunction<T, R> {
    R apply(T t) throws JMSException;
}
