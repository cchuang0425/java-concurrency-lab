package org.iii.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class BaseWorker {
    protected <T> T waitAndTake(LinkedBlockingQueue<T> channel) {
        try {
            return channel.take();
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }
}
