package org.iii.math;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrimalityFiberValue {
    private long n;
    private long fib;
    private boolean prime;
}
