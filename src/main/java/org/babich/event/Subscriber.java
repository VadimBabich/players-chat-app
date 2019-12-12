package org.babich.event;

import org.babich.event.api.Listener;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A subscriber is an internally executor that should be used for dispatching events to Listener.
 *
 *  @author Vadim Babich
 */
class Subscriber {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final EventBus bus;
    private final Listener listener;
    private final Executor executor;

    Subscriber(EventBus bus, Listener listener) {
        this.bus = bus;
        this.listener = listener;
        this.executor = bus.getExecutor();
    }

    final void dispatchEvent(final String event) {
        synchronized (this) {
            executor.execute( () -> {

                            try {
                                listener.onMessage(event);
                            } catch (Exception e) {
                                logger.log(Level.SEVERE
                                        , String.format("Cannot dispatch event {%s}.", event)
                                        , e.getCause());
                            }

                    });
        }
    }

    Listener getListener() {
        return listener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscriber that = (Subscriber) o;
        return bus.equals(that.bus) &&
                listener.equals(that.listener) &&
                executor.equals(that.executor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bus, listener, executor);
    }
}
