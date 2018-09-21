package org.iii.fib;

import org.springframework.stereotype.Service;

@Service
public class FibService {

    public long calFibRec(long n) {
        if (n == 0 || n == 1) {
            return n;
        } else {
            return calFibRec(n - 1) + calFibRec(n - 2);
        }
    }

    public long calFibIter(long n) {
        long a = 0;
        long b = 1;

        for (long i = n; i > 0; i--) {
            long temp = a;
            a = a + b;
            b = temp;
        }

        return a;
    }
}
