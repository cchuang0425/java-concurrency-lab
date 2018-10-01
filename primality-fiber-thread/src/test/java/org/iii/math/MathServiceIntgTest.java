package org.iii.math;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MathServiceIntgTest {

    @Autowired
    private MathService service;

    @Test
    public void testCalFib() throws InterruptedException {
        LinkedBlockingQueue<Long> channel = service.calFib(10);
        Long fibResult = channel.take();
        Assert.assertEquals(55, fibResult.longValue());
    }

    @Test
    public void testCalPrime() throws InterruptedException {
        LinkedBlockingQueue<Boolean> channel = service.calPrime(37);
        Boolean primeResult = channel.take();
        Assert.assertTrue(primeResult);
    }
}
