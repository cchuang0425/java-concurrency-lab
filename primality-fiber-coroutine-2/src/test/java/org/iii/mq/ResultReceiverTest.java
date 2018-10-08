package org.iii.mq;

import java.sql.Time;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.iii.PrimalityFiberConfig;

public class ResultReceiverTest {

    @Before
    public void setUp() {
        PrimalityFiberConfig.run();
    }

    @Test
    public void testReceiveResult() throws InterruptedException {
        ResultReceiver receiver = ResultReceiver.getInstance();
        ExecutorService pool = PrimalityFiberConfig.getThreadPool();
        pool.execute(receiver);
        pool.shutdown();

        TimeUnit.MILLISECONDS.sleep(3000L);
    }

    @After
    public void tearDown() {
        PrimalityFiberConfig.exit();
    }
}
