package org.iii;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.iii.fib.FibService;
import org.iii.prime.PrimeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrimalityFibTest {

    @Autowired
    private FibService fibService;

    @Autowired
    private PrimeService primeService;

    @Test
    public void testPrimalityFib() {
        long n = 45;
        List<Long> numbers = new ArrayList<>();

        for (long i = 1; i <= n; i++) {
            long fibNum = fibService.calFibRec(i);
            System.out.printf("fib: %d -> %d ", i, fibNum);
            if (primeService.isPrime(fibNum)) {
                System.out.printf("is prime.%n");
                numbers.add(i);
            } else {
                System.out.printf("is not prime.%n");
            }
        }

        System.out.println(numbers);
    }
}
