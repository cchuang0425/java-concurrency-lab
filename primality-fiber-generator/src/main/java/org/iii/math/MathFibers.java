package org.iii.math;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.iii.PrimalityFiberConfig;
import org.iii.common.GenericGenerator;
import org.iii.util.LambdaUtils;

import static org.iii.PrimalityFiberConfig.WORKER_POOL_NAME;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

public class MathFibers {

    public static Channel<Long> runFibGenerator() {
        Channel<Long> channel = Channels.newChannel(1);

        ApplicationContext context = PrimalityFiberConfig.ApplicationContextProvider.getApplicationContext();
        FiberScheduler pool = context.getBean(WORKER_POOL_NAME, FiberScheduler.class);
        MathService service = context.getBean(MathService.class);
        FibGenerator generator = context.getBean(FibGenerator.class, channel, service);

        Fiber<Void> fiber = new Fiber<>(pool, generator);
        fiber.start();

        return channel;
    }

    @Component
    @Scope(SCOPE_PROTOTYPE)
    public static class FibGenerator extends GenericGenerator<Long, Long> {
        protected FibGenerator(Channel<Long> channel, MathService service) {
            super(channel, 0L, Long.MAX_VALUE, LambdaUtils::add1, service::calFib);
        }
    }
}
