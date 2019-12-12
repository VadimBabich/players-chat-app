package org.babich.event;

import org.babich.event.Subscriber;

import java.util.Iterator;

/**
 * Handler for dispatching events to subscribers.
 *
 *   @author Vadim Babich
 */
abstract class Dispatcher {

    /**
     * Dispatching event to subscribers
     * @param event event
     * @param subscribers subscribers
     */
    abstract void dispatch(String event, Iterator<Subscriber> subscribers);
}
