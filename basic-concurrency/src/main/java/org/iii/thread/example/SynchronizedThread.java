package org.iii.thread.example;

public class SynchronizedThread {
    public static void main(String[] args) {
        synchronized (SynchronizedThread.class){
            method();
        }
    }

    public static synchronized void method(){}
}
