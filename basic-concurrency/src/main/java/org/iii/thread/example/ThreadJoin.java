package org.iii.thread.example;

public class ThreadJoin {
    public static void main(String[] args) {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Domino(previous), "Thread-" + i);
            thread.start();
            previous = thread;
        }
    }

    static class Domino implements Runnable {
        private Thread thread;

        public Domino(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException ex) {}

            System.out.printf("%s terminate. %n", Thread.currentThread().getName());
        }
    }
}
