package org.iii.thread.example;

import java.util.concurrent.TimeUnit;

public class ThreadShutdown {
    public static void main(String[] args) throws InterruptedException {
        Runner one = new Runner();
        Thread countThread = new Thread(one, "CountThread");
        countThread.start();

        TimeUnit.SECONDS.sleep(1L);
        countThread.interrupt();

        Runner two = new Runner();
        countThread = new Thread(two, "CountThread");
        countThread.start();

        TimeUnit.SECONDS.sleep(1L);
        two.cancel();
    }

    static class Runner implements Runnable {
        private long i = 0L;
        private volatile boolean on = true;

        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                i++;
            }

            System.out.println("Count i = " + i);
        }

        public void cancel() {
            on = false;
        }
    }
}
