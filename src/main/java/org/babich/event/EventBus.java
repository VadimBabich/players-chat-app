package org.babich.event;

import org.babich.event.api.EventBusAware;
import org.babich.event.api.Listener;
import org.babich.event.api.Publisher;

import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Dispatches events to listeners, and provides ways for listeners to register themselves.
 * The EventBus allows publish-subscribe-style communication.
 *
 *  @author Vadim Babich
 */
public class EventBus {

    private final String chatName;
    private final AtomicBoolean isEmpty = new AtomicBoolean(true);

    private final Dispatcher dispatcher;
    private final ExecutorService executor;
    private final CopyOnWriteArraySet<Subscriber> subscribers = new CopyOnWriteArraySet<>();

    public EventBus(String chatName, ExecutorService executor) {
        this.dispatcher = new BroadcastingDispatcher();
        this.executor = executor;
        this.chatName = chatName;
    }

    Executor getExecutor() {
        return executor;
    }

    public String getChatName() {
        return chatName;
    }

    public void post(Publisher publisher) {
        post(publisher.getPayload(), publisher.getExcludes());
    }

    public void subscribe(Listener listener) {

        subscribers.add(new Subscriber(this, listener));

        isEmpty.set(false);

        if (listener instanceof EventBusAware) {
            ((EventBusAware) listener).onSubscribe(this);
        }
    }


    public void unsubscribe(Listener listener) {

        Set<Subscriber> subscribersToRemove =
                subscribers.stream()
                        .filter(subscriber -> listener.equals(subscriber.getListener()))
                        .collect(Collectors.toSet());

        subscribers.removeAll(subscribersToRemove);

        isEmpty.compareAndSet(false, subscribers.isEmpty());

        if (listener instanceof EventBusAware) {
            ((EventBusAware) listener).onUnsubscribe(this);
        }
    }


    public boolean isEmpty() {
        return isEmpty.get();
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", EventBus.class.getSimpleName() + "[", "]")
                .add("chatName='" + chatName + "'")
                .toString();
    }


    private void post(String message, Predicate<Listener> excludes) {
        Predicate<Listener> predicate = (null == excludes) ? (s) -> true : excludes.negate();

        Set<Subscriber> subscribers = this.subscribers.stream()
                .filter(subscriber -> predicate.test(subscriber.getListener()))
                .collect(Collectors.toSet());

        dispatcher.dispatch(message, subscribers.iterator());
    }

}
