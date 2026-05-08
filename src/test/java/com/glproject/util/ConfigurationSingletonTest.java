package com.glproject.util;

import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationSingletonTest {

    @Test
    void getInstance_returnsSameInstance() {
        Configuration c1 = Configuration.getInstance();
        Configuration c2 = Configuration.getInstance();
        assertSame(c1, c2);
    }

    @Test
    void getInstance_isThreadSafe() throws InterruptedException {
        int threadCount = 10;
        Configuration[] instances = new Configuration[threadCount];
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
                instances[index] = Configuration.getInstance();
            });
            threads[i].start();
        }

        latch.countDown();
        for (Thread t : threads) {
            t.join();
        }

        Configuration baseline = Configuration.getInstance();
        for (Configuration instance : instances) {
            assertSame(baseline, instance);
        }
    }

    @Test
    void get_returnsDefaultValue() {
        Configuration config = Configuration.getInstance();
        assertEquals("DocumentBuilder", config.get("app.name"));
        assertEquals("pdf", config.get("export.default.format"));
    }

    @Test
    void setAndGet_updatesValue() {
        Configuration config = Configuration.getInstance();
        config.set("test.key", "test.value");
        assertEquals("test.value", config.get("test.key"));
    }
}
