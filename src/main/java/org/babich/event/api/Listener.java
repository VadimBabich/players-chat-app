package org.babich.event.api;

/**
 * This interface is used to subscribe to messages.
 *
 *   @author Vadim Babich
 */
public interface Listener {

    /**
     * The method is the consumer of the text message payload.
     * @param message payload
     * @throws InterruptedException
     */
    void onMessage(String message) throws InterruptedException;
}
