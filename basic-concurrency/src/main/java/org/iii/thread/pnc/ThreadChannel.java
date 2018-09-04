package org.iii.thread.pnc;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadChannel {
    private static final ThreadLocal<Instant> TIMER = ThreadLocal.withInitial(Instant::now);
    private static final ChannelManager CHANNEL_MANAGER = new ChannelManager();
    private static final ExecutorService MAIN_POOL = Executors.newFixedThreadPool(3);
    private static final ExecutorService STEP_1_POOL = Executors.newFixedThreadPool(3);
    private static final ExecutorService STEP_2_POOL = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 20; i++) {
            MAIN_POOL.execute(new Worker());
        }

        TimeUnit.SECONDS.sleep(20L);

        MAIN_POOL.shutdown();
        STEP_1_POOL.shutdown();
        STEP_2_POOL.shutdown();
    }

    private static final int MAX_SLEEP = 5000;
    private static final long BASE_SLEEP = 100L;

    private static Long randomSleepTime() {
        return BASE_SLEEP + ((new Random()).nextInt(MAX_SLEEP));
    }

    private static Channel<String> runStep1(String parentThreadName) {
        Channel<String> step1Result = new Channel<>(1);

        STEP_1_POOL.execute(new Step1(CHANNEL_MANAGER.register(UUID.randomUUID(), step1Result), parentThreadName));

        return step1Result;
    }

    private static Channel<Long> runStep2(String parentThreadName) {
        Channel<Long> step2Result = new Channel<>(1);

        STEP_2_POOL.execute(new Step2(CHANNEL_MANAGER.register(UUID.randomUUID(), step2Result), parentThreadName));

        return step2Result;
    }

    public static class Step1 implements Runnable {

        private UUID id;
        private String parent;

        public Step1(UUID id, String parent) {
            this.id = id;
            this.parent = parent;
        }

        @Override
        public void run() {
            try {
                System.out.printf("=== Step1: %s running.%n", threadName());
                TimeUnit.MILLISECONDS.sleep(showSleep(randomSleepTime()));
                ((Channel<String>) CHANNEL_MANAGER.take(id)).send("work finished at " + Instant.now());
                System.out.printf("=== Step1: %s finished at %s.%n", threadName(), Instant.now());
            } catch (InterruptedException ex) { }
        }

        private String threadName() {
            return Thread.currentThread().getName() + "(" + this.parent + ")";
        }

        private long showSleep(long sleepTime) {
            System.out.printf("=== Step1: %s is sleeping with %d milli.%n", threadName(), sleepTime);
            return sleepTime;
        }
    }

    public static class Step2 implements Runnable {
        private UUID id;
        private String parent;

        public Step2(UUID id, String parent) {
            this.id = id;
            this.parent = parent;
        }

        @Override
        public void run() {
            try {
                System.out.printf("$$$ Step2: %s running.%n", threadName());
                TimeUnit.MILLISECONDS.sleep(showSleep(randomSleepTime()));
                ((Channel<Long>) CHANNEL_MANAGER.take(this.id)).send(System.currentTimeMillis());
                System.out.printf("$$$ Step2: %s finished at %s.%n", threadName(), Instant.now());
            } catch (InterruptedException ex) {}
        }

        private String threadName() {
            return Thread.currentThread().getName() + "(" + this.parent + ")";
        }

        private long showSleep(long sleepTime) {
            System.out.printf("$$$ Step2: %s is sleep %d milli.%n", threadName(), sleepTime);
            return sleepTime;
        }
    }

    public static class Worker implements Runnable {
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();

            System.out.printf("### %s start at: %s.%n", threadName, TIMER.get());

            System.out.printf("### %s running step 1.....%n", threadName);

            try {
                Channel<String> step1Result = runStep1(threadName);
                System.out.printf("### %s Step1 result: %s%n", threadName, step1Result.receive());
            } catch (InterruptedException ex) {}

            System.out.printf("### %s running step 2.....%n", threadName);

            try {
                Channel<Long> step2Result = runStep2(threadName);
                System.out.printf("### %s Step2 result: %s%n", threadName, Instant.ofEpochMilli(step2Result.receive()));
            } catch (InterruptedException ex) {}

            System.out.printf("### %s finish at: %s.%n", threadName, TIMER.get());
        }
    }

}
