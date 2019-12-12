package org.babich.event.api;

import org.babich.event.EventBus;

/**
 * Interface to be implemented by any listener that wishes to be notified of
 * the {@link EventBus} that it subscribe/unsubscribe from the bus.
 *
 *  @author Vadim Babich
 */
public interface EventBusAware {

    /**
     * Set the EventBus that this object runs in.
     *
     * @param bus event bus
     */
    void onSubscribe(EventBus bus);

    /**
     * Remove the EventBus from this object.
     * @param bus  Removed bus
     */
    void onUnsubscribe(EventBus bus);
}
