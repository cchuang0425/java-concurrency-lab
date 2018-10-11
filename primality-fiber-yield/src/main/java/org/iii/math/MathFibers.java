package org.iii.math;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.LongChannel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.iii.PrimalityFiberConfig;

import static org.iii.PrimalityFiberConfig.WORKER_POOL_NAME;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

public class MathFibers {
    public static LongChannel runCounterGenerator() {
        LongChannel channel = Channels.newLongChannel(1);

        ApplicationContext context = PrimalityFiberConfig.ApplicationContextProvider.getApplicationContext();
        FiberScheduler pool = context.getBean(WORKER_POOL_NAME, FiberScheduler.class);
        CounterGenerator generator = context.getBean(CounterGenerator.class, channel);

        Fiber<Void> fiber = new Fiber<>(pool, generator);
        fiber.start();

        return channel;
    }

    @Component
    @Scope(SCOPE_PROTOTYPE)
    public static class CounterGenerator implements SuspendableRunnable {

        private LongChannel channel;

        public CounterGenerator(LongChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() throws SuspendExecution, InterruptedException {
            long current = 1L;

            while (true) {
                this.channel.send(current);
                current++;
            }
        }
    }
}
