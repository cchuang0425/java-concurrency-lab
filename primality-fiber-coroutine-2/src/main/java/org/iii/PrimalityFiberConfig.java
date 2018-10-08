package org.iii;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import co.paralleluniverse.common.monitoring.MonitorType;
import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.FiberScheduler;

public class PrimalityFiberConfig {
    public static final String MQ_HOST = "localhost";
    public static final int MQ_PORT = 61616;
    public static final int MQ_POOL = 10;
    public static final String MQ_PATTERN = "tcp://%s:%d";
    public static final String COMMAND_QUEUE = "CommandQueue";
    public static final String RESULT_QUEUE = "ResultQueue";
    public static final long MQ_WAIT = 1000L;

    private static FiberScheduler fiberPool;

    public static FiberScheduler getFiberPool() {
        if (fiberPool == null) {
            fiberPool = new FiberForkJoinScheduler("math-", 10, MonitorType.NONE, false);
        }
        return fiberPool;
    }

    private static ExecutorService threadPool;

    public static ExecutorService getThreadPool() {
        if (threadPool == null) {
            threadPool = Executors.newCachedThreadPool();
        }
        return threadPool;
    }

    public static final int STATE_INIT = 0;
    public static final int STATE_RUN = 1;
    public static final int STATE_EXIT = -1;

    private static int state = STATE_INIT;

    public static void run() {
        state = STATE_RUN;
    }

    public static void exit() {
        state = STATE_EXIT;
    }

    public static void reset() {
        state = STATE_INIT;
    }

    public static int getState() {
        return state;
    }
}
