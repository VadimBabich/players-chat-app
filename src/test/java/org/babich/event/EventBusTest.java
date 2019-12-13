package org.babich.event;


import org.babich.event.api.EventBusAware;
import org.babich.event.api.Listener;
import org.babich.event.api.Publisher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.IsEqual.equalTo;


public class EventBusTest {

    private EventBus underTest;
    private final String expectedMessage = "TEST";

    @Before
    public void setUp() {
        underTest = new EventBus("test", Executors.newFixedThreadPool(1));
    }

    /**
     * Add two subscribers to the message bus and check how they receive the message.
     */
    @Test
    public void post() {

        AtomicInteger countDown = new AtomicInteger(2);
        List<Runnable> errors = new ArrayList<>();

        underTest.subscribe(message -> {
            errors.add(() -> Assert.assertEquals(expectedMessage, message));
            countDown.decrementAndGet();
        });

        underTest.subscribe(message -> {
            errors.add(() -> Assert.assertEquals(expectedMessage, message));
            countDown.decrementAndGet();
        });

        underTest.post(new DefaultPublisher(expectedMessage));

        await("countDown").atMost(200, TimeUnit.MILLISECONDS)
                .untilAtomic(countDown, equalTo(0));

        errors.forEach(Runnable::run);
    }

    /**
     * Post messages on the event bus and unsubscribe from it through 10 messages.
     */
    @Test
    public void postInLoop() {

        AtomicInteger countDown = new AtomicInteger(10);

        List<Runnable> errors = new ArrayList<>();

        underTest.post(new DefaultPublisher(expectedMessage));
        underTest.subscribe(new Listener() {

            final AtomicInteger counter = new AtomicInteger();

            @Override
            public void onMessage(String message) {
                countDown.decrementAndGet();

                if (10 <= counter.incrementAndGet()) {
                    underTest.unsubscribe(this);
                }

                underTest.post(new DefaultPublisher(expectedMessage));

                errors.add(() -> Assert.assertEquals(expectedMessage, message));
            }
        });

        underTest.post(new DefaultPublisher(expectedMessage));

        await("countDown")
                .atMost(200, TimeUnit.MILLISECONDS)
                .untilAtomic(countDown, equalTo(0));

        errors.forEach(Runnable::run);
    }

    /**
     * Sending one message to the event bus and unsubscribe immediately.
     * Waiting for an emptied eventbus after.
     */
    @Test
    public void isEmptyBus() {

        AtomicBoolean messageReceived = new AtomicBoolean();
        AtomicInteger countDown = new AtomicInteger(10);
        AtomicBoolean isEmpty = new AtomicBoolean();
        AtomicBoolean onUnsubscribe = new AtomicBoolean();

        List<Runnable> errors = new ArrayList<>();

        underTest.subscribe(new BusAwareListener() {
            @Override
            public void onMessage(String message) {
                messageReceived.set(true);

                await("countDown")
                        .pollInterval(Duration.ofMillis(1))
                        .atMost(Duration.ofMillis(400))
                        .untilAtomic(countDown, equalTo(0));

                underTest.unsubscribe(this);
            }

            @Override
            public void onUnsubscribe(EventBus bus) {
                onUnsubscribe.set(true);
            }
        });

        underTest.post(new DefaultPublisher(expectedMessage));

        new Thread(() -> {
            try {
                await("messageReceived")
                        .pollInterval(Duration.ofMillis(1))
                        .atMost(Duration.ofMillis(400))
                        .untilTrue(messageReceived);

                for (; ; ) {
                    countDown.decrementAndGet();

                    if (underTest.isEmpty()) {
                        errors.add(() -> Assert.assertTrue(onUnsubscribe.get()));
                        isEmpty.set(true);
                        break;
                    }

                    Thread.sleep(1L);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

        await("isEmpty")
                .atMost(Duration.ofMillis(400))
                .untilTrue(isEmpty);

        await("onUnsubscribe")
                .atMost(Duration.ofMillis(400))
                .untilTrue(onUnsubscribe);

        errors.forEach(Runnable::run);

    }


    private static class BusAwareListener implements Listener, EventBusAware {

        @Override
        public void onSubscribe(EventBus bus) {

        }

        @Override
        public void onUnsubscribe(EventBus bus) {

        }

        @Override
        public void onMessage(String message) {

        }
    }

    private static class DefaultPublisher implements Publisher {

        final String payload;

        private DefaultPublisher(String payload) {
            this.payload = payload;
        }

        @Override
        public String getPayload() {
            return payload;
        }

        @Override
        public Predicate<Listener> getExcludes() {
            return null;
        }
    }
}