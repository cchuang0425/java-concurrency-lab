package org.iii.util;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class LambdaUtils {
    public static <T> T take(LinkedBlockingQueue<T> channel, T deft) {
        try {
            return channel.take();
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
            return deft;
        }
    }

    public static <T> T take(LinkedBlockingQueue<T> channel) {
        try {
            return channel.take();
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }
}
