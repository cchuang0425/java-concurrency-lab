package org.iii.prime;

import org.springframework.stereotype.Service;

@Service
public class PrimeService {
    public boolean isPrime(long value) {
        if (value <= 1) {
            return false;
        } else {
            for (long div = value - 1; div > 0; div--) {
                if ((value % div) == 0) { return div == 1; }
            }

            return false;
        }
    }
}
