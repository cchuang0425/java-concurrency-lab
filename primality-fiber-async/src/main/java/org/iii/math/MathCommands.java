package org.iii.math;

import java.util.UUID;

import org.iii.domain.CommandMessage;

public class MathCommands {
    public static final String FIB_COMMAND = "FibCommand";

    public static CommandMessage<Long> createFibCommand(long n) {
        return CommandMessage.<Long>builder().id(UUID.randomUUID())
                                             .name(FIB_COMMAND)
                                             .param(n)
                                             .build();
    }

    public static final String PRIME_COMMAND = "PrimeCommand";

    public static CommandMessage<Long> createPrimeCommand(long n) {
        return CommandMessage.<Long>builder().id(UUID.randomUUID())
                                             .name(PRIME_COMMAND)
                                             .param(n)
                                             .build();
    }
}
