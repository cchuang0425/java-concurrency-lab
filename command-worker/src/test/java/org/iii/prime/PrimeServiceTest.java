package org.iii.prime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrimeServiceTest {

    @Autowired
    private PrimeService service;

    @Test
    public void testIsPrime(){
        long notPrime = 1;
        long minPrime = 2;
        long valuePrime = 37;
        long valueDouble = 38;
        long valueThrible = 39;

        Assert.assertFalse(service.isPrime(notPrime));
        Assert.assertTrue(service.isPrime(minPrime));
        Assert.assertTrue(service.isPrime(valuePrime));
        Assert.assertFalse(service.isPrime(valueDouble));
        Assert.assertFalse(service.isPrime(valueThrible));
    }
}
