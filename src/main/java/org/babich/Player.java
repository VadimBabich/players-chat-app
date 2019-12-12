package org.babich;

import org.babich.event.EventBus;
import org.babich.event.api.EventBusAware;
import org.babich.event.api.Listener;
import org.babich.event.api.Publisher;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

/**
 * The player is both a subscriber and a publisher of messages.
 * It also counts the iterations of receiving and sending the message.
 *
 *  @author Vadim Babich
 */
public class Player implements Listener, EventBusAware {

    private final AtomicReference<EventBus> bus = new AtomicReference<>();

    private final AtomicInteger sendCounter = new AtomicInteger();
    private final AtomicInteger receiveCounter = new AtomicInteger();

    public Player(String nicName) {
        this.nicName = nicName;
    }

    public final String nicName;

    @Override
    public void onMessage(String message) {

        System.out.println(String.format("thread_id: %d player: %s received message: %s"
                , Thread.currentThread().getId()
                , nicName
                , message));

        receiveCounter.incrementAndGet();

        send(message);
    }


    public void send(String message) {

        EventBus eventBus;
        if (null == (eventBus = bus.get())) {
            return;
        }

        int sendCount;
        if (10 <= (sendCount = sendCounter.incrementAndGet()) && 10 <= receiveCounter.get()) {

            eventBus.unsubscribe(this);
        }

        eventBus.post(new Publisher() {
            @Override
            public String getPayload() {
                String newMessage = message + "," + sendCount;

                System.out.println(String.format("thread_id: %d player: %s send message: %s"
                        , Thread.currentThread().getId()
                        , nicName
                        , message));

                return newMessage;
            }

            @Override
            public Predicate<Listener> getExcludes() {
                return predicate;
            }
        });

    }


    @Override
    public void onSubscribe(EventBus bus) {
        this.bus.set(bus);
    }

    @Override
    public void onUnsubscribe(EventBus bus) {
        this.bus.set(null);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
                .add("bus=" + bus)
                .add("id='" + nicName + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return nicName.equals(player.nicName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nicName);
    }

    private final Predicate<Listener> predicate = (listener) -> listener.equals(Player.this);

}
