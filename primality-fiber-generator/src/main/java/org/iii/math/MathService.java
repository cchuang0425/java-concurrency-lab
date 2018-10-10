package org.iii.math;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.stereotype.Service;

@Service
public class MathService {
    public long calFib(long n) {
        if (n == 0 || n == 1) {
            return n;
        } else {
            return calFib(n - 1) + calFib(n - 2);
        }
    }

    public boolean isPrime(long n) {
        if (n <= 1) {
            return false;
        } else {
            for (long div = n - 1; div > 0; div--) {
                if ((n % div) == 0) {return div == 1;}
            }

            return false;
        }
    }
}
