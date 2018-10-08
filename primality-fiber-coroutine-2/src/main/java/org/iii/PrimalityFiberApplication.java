package org.iii;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;

import org.iii.math.PrimalityFiber;
import org.iii.mq.ResultReceiver;

public class PrimalityFiberApplication {

    public static final long N = 45;

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            return;
        } else {
            System.out.println("run PrimalityFiber...");

            PrimalityFiberConfig.run();

            Thread resultReceiver = new Thread(ResultReceiver.getInstance());
            ExecutorService pool = PrimalityFiberConfig.getThreadPool();
            pool.execute(resultReceiver);
            pool.shutdown();

            PrimalityFiber primalityFiber = new PrimalityFiber(N);
            FiberScheduler scheduler = PrimalityFiberConfig.getFiberPool();
            Fiber<List<Long>> fiber = scheduler.newFiber(primalityFiber);
            scheduler.shutdown();

            try {
                List<Long> primes = fiber.start().get();

                System.out.println(primes);

                System.exit(0);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace(System.err);

                System.exit(1);
            }
        }
    }
}
