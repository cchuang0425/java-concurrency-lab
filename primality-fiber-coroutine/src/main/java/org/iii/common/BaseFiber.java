package org.iii.common;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.Channel;

public abstract class BaseFiber {
    @Suspendable
    protected <T> T receiveFromChannel(Channel<T> channel) throws InterruptedException, SuspendExecution {
        return channel.receive();
    }
}
