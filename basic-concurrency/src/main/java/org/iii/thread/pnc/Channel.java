package org.iii.thread.pnc;

import java.util.LinkedList;

public class Channel<T> {
    private int size = -1;
    private boolean unlimited = false;
    private LinkedList<T> queue = new LinkedList<>();

    public Channel(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size must be greater than or equal to 0");
        } else if (size == 0) {
            unlimited = true;
        }

        this.size = size;
    }

    public synchronized void send(T message) throws InterruptedException {
        while (!unlimited && queue.size() >= size) {
            wait();
        }

        queue.addLast(message);

        notifyAll();
    }

    public synchronized T receive() throws InterruptedException {
        while (queue.size() <= 0) {
            wait();
        }

        T message = queue.removeFirst();

        notifyAll();

        return message;
    }
}
