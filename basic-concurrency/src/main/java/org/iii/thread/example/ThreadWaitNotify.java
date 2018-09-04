package org.iii.thread.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ThreadWaitNotify {
    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws Exception {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();

        TimeUnit.SECONDS.sleep(1);

        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
    }

    static class Wait implements Runnable {
        @Override
        public void run() {
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");

            synchronized (lock) {
                while (flag) {
                    try {
                        System.out
                                .printf("%s flag is true. wait @ %s%n", Thread.currentThread(), fmt.format(new Date()));
                        lock.wait();
                    } catch (InterruptedException ex) {}
                }

                System.out.printf("%s flag is false. running @ %s%n", Thread.currentThread(), fmt.format(new Date()));
            }
        }
    }

    static class Notify implements Runnable {
        @Override
        public void run() {
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");

            synchronized (lock) {
                System.out.printf("%s hold lock. notify @ %s%n", Thread.currentThread(), fmt.format(new Date()));
                lock.notifyAll();
                flag = false;
                SleepUtils.second(5);
            }

            synchronized (lock) {
                System.out.printf("%s hold lock again. sleep @ %s%n", Thread.currentThread(), fmt.format(new Date()));
                SleepUtils.second(5);
            }
        }
    }
}
