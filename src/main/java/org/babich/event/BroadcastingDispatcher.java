package org.babich.event;


import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 *   @author Vadim Babich
 */
class BroadcastingDispatcher extends Dispatcher {

    private final ThreadLocal<Queue<Event>> queue = ThreadLocal.withInitial(ArrayDeque::new);

    private final ThreadLocal<Boolean> dispatching = ThreadLocal.withInitial(() -> false);

    @Override
    void dispatch(String event, Iterator<Subscriber> subscribers) {
        Queue<BroadcastingDispatcher.Event> queueForThread = queue.get();
        queueForThread.offer(new BroadcastingDispatcher.Event(event, subscribers));

        if (!dispatching.get()) {
            dispatching.set(true);
            try {
                BroadcastingDispatcher.Event nextEvent;
                while ((nextEvent = queueForThread.poll()) != null) {

                    for(;nextEvent.subscribers.hasNext();){
                        nextEvent.subscribers.next().dispatchEvent(nextEvent.payload);
                    }
                }
            } finally {
                dispatching.remove();
                queue.remove();
            }
        }
    }

    private static final class Event {
        private final String payload;
        private final Iterator<Subscriber> subscribers;

        private Event(String event, Iterator<Subscriber> subscribers) {
            this.payload = event;
            this.subscribers = subscribers;
        }
    }
}
