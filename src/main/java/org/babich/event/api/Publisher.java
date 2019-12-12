package org.babich.event.api;

import java.util.function.Predicate;

/**
 * Interface for sending text messages to consumers.
 *
 *   @author Vadim Babich
 */
public interface Publisher {

    /**
     * Sending text messages
     */
    String getPayload();

    /**
     * Exclude items from the consumer list for messages from this pusher.
     * @return predicate
     */
    Predicate<Listener> getExcludes();
}
