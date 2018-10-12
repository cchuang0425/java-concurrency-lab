package org.iii.common;

import java.util.function.Function;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.channels.Channel;

public abstract class GenericGenerator<T extends Comparable<T>, R> implements SuspendableRunnable {

    private Channel<R> channel;
    private Function<T, T> increator;
    private Function<T, R> processor;
    private T init;
    private T limit;

    protected GenericGenerator(Channel<R> channel, T init, T limit, Function<T, T> increator,
            Function<T, R> processor) {
        this.channel = channel;
        this.increator = increator;
        this.processor = processor;
        this.init = init;
        this.limit = limit;
    }

    @Override
    public void run() throws SuspendExecution, InterruptedException {
        while (init.compareTo(limit) <= 0) {
            init = increator.apply(init);
            channel.send(processor.apply(init));
        }
    }
}
