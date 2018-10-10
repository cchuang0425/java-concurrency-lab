package org.iii.math;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.LongChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.iii.PrimalityFiberConfig;

import static org.iii.PrimalityFiberConfig.WORKER_POOL_NAME;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

public class MathFibers {

    public static LongChannel runFibGenerator() {
        LongChannel channel = Channels.newLongChannel(1);

        ApplicationContext context = PrimalityFiberConfig.ApplicationContextProvider.getApplicationContext();
        FiberScheduler pool = context.getBean(WORKER_POOL_NAME, FiberScheduler.class);
        FibGenerator generator = context.getBean(FibGenerator.class, channel);

        Fiber<Void> fiber = new Fiber<>(pool, generator);
        fiber.start();

        return channel;
    }

    @Component
    @Scope(SCOPE_PROTOTYPE)
    public static class FibGenerator implements SuspendableRunnable {

        @Autowired
        private MathService service;

        private LongChannel channel;

        public FibGenerator(LongChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() throws SuspendExecution, InterruptedException {
            long current = 1L;

            while (true) {
                channel.send(service.calFib(current));
                current++;
            }
        }
    }
}
