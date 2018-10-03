package org.iii;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.LongStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import org.iii.common.ChannelRegistry;
import org.iii.math.MathService;

import static java.util.stream.Collectors.toList;
import static org.iii.util.LambdaUtils.take;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrimalityFiberApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MathService service;

    @Autowired
    private ChannelRegistry channels;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testUsingReactor() {
        Scheduler scheduler = Schedulers.elastic();

        List<Long> primes = Flux.range(1, 45)
                                .subscribeOn(scheduler)
                                .map(n -> getChannelAndExecFib(Long.valueOf(n)))
                                .map(tup -> Tuples.of(tup.getT1(), take(tup.getT2())))
                                .filter(Tuple2::getT2)
                                .map(Tuple2::getT1)
                                .collect(toList())
                                .block();

        System.out.println(primes);
    }

    @Test
    public void testUsingStreamAPI() {
        List<Long> primeRes = LongStream.rangeClosed(1, 45)
                                        .mapToObj(n -> Tuples.of(n, service.sendFib(n)))
                                        .map(tup -> Tuples.of(tup.getT1(), take(tup.getT2())))
                                        .map(tup -> Tuples.of(tup.getT1(), service.sendPrime(tup.getT2())))
                                        .map(tup -> Tuples.of(tup.getT1(), take(tup.getT2())))
                                        .filter(Tuple2::getT2)
                                        .map(Tuple2::getT1)
                                        .collect(toList());

        System.out.println(primeRes);
    }

    @Test
    public void testUsingEventDriven() {
        List<Long> primes = LongStream.rangeClosed(1, 45)
                                      .mapToObj(this::getChannelAndExecFib)
                                      .map(tup -> Tuples.of(tup.getT1(), take(tup.getT2())))
                                      .filter(Tuple2::getT2)
                                      .map(Tuple2::getT1)
                                      .collect(toList());

        System.out.println(primes);
    }

    private Tuple2<Long, LinkedBlockingQueue<Boolean>> getChannelAndExecFib(Long n) {
        LinkedBlockingQueue<Boolean> channel = channels.create(n);
        service.execFibCommand(n);
        return Tuples.of(n, channel);
    }

    @Test
    public void testUsingCompletableFuture() {

    }
}
