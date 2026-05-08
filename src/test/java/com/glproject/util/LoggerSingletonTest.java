package com.glproject.util;

import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.*;

class LoggerSingletonTest {

    @Test
    void getInstance_returnsSameInstance() {
        Logger l1 = Logger.getInstance();
        Logger l2 = Logger.getInstance();
        assertSame(l1, l2);
    }

    @Test
    void getInstance_isThreadSafe() throws InterruptedException {
        int threadCount = 10;
        Logger[] instances = new Logger[threadCount];
        Thread[] threads = new Thread[threadCount];
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < threadCount; i++) {
            int index = i;
            threads[i] = new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                instances[index] = Logger.getInstance();
            });
            threads[i].start();
        }

        latch.countDown();
        for (Thread t : threads) {
            t.join();
        }

        Logger baseline = Logger.getInstance();
        for (Logger instance : instances) {
            assertSame(baseline, instance);
        }
    }
}
