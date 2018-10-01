package org.iii;

import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.eventbus.EventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import org.iii.domain.CommandMessage;
import org.iii.domain.ResultMessage;
import org.iii.math.MathCommands;
import org.iii.math.MathService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrimalityFiberApplicationTests {

    @Autowired
    private MathService mathService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testSubscribeNumbers() {
        Flux<Integer> numbers = Flux.range(1, 10);
        numbers.subscribe(System.out::println);
    }

    @Test
    public void testCreateNumbers() {
        Flux.range(1, 10)
            .subscribe(i -> mathService.sendFib((long) i),
                    error -> error.printStackTrace(System.err),
                    () -> {});
    }

}
