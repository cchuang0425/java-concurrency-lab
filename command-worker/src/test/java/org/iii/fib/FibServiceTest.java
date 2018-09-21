package org.iii.fib;

import java.util.function.LongFunction;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FibServiceTest {

    @Autowired
    private FibService fibService;

    @Test
    public void testCalFibRec() {
        testTemplate(fibService::calFibRec, 10, 55);
        testTemplate(fibService::calFibRec, 15, 610);
        testTemplate(fibService::calFibRec, 20, 6765);
    }

    @Test
    public void testCalFibIter() {
        testTemplate(fibService::calFibIter, 10, 55);
        testTemplate(fibService::calFibIter, 15, 610);
        testTemplate(fibService::calFibIter, 20, 6765);
    }

    private void testTemplate(LongFunction<Long> fibFunc, long n, long expect) {
        long actural = fibFunc.apply(n);
        Assert.assertEquals(expect, actural);
    }
}
