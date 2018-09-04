package org.iii.thread.example;

import java.util.concurrent.TimeUnit;

public class ThreadLocalTest {

    private static final ThreadLocal<Long> TIME_THREAD_LOCAL =
            ThreadLocal.withInitial(() -> System.currentTimeMillis());

    public static final void begin() {
        TIME_THREAD_LOCAL.set(System.currentTimeMillis());
    }

    public static final long end() {
        return System.currentTimeMillis() - TIME_THREAD_LOCAL.get();
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalTest.begin();

        TimeUnit.SECONDS.sleep(1L);

        System.out.printf("Cost: %d mills.", ThreadLocalTest.end());
    }
}
