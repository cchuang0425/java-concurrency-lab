package org.iii.math;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import org.iii.common.Cache;
import org.iii.common.ChannelRegistry;
import org.iii.domain.Command;
import org.iii.domain.CommandMessage;
import org.iii.domain.ResultMessage;
import org.iii.mq.CommandSender;

import static org.iii.util.JsonUtils.convertJsonFromObject;

@Service
public class MathService {

    @Autowired
    private CommandSender sender;

    @Autowired
    private ChannelRegistry channels;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SystemCache caches;

    public LinkedBlockingQueue<Long> sendFib(long n) {
        CommandMessage<Long> fibCmd = MathCommands.createFibCommand(n);

        try {
            sender.sendCommand(convertJsonFromObject(fibCmd));
            return channels.create(fibCmd.getId(), Long.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public LinkedBlockingQueue<Boolean> sendPrime(long n) {
        CommandMessage<Long> primeCmd = MathCommands.createPrimeCommand(n);

        try {
            sender.sendCommand(convertJsonFromObject(primeCmd));
            return channels.create(primeCmd.getId(), Boolean.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void resumeCommand(ResultMessage<?> rawMessage) {
        switch (rawMessage.getName()) {
            case MathCommands.FIB_COMMAND:
                resume((ResultMessage<Long>) rawMessage, caches.getFibCache(), Long.class);
                break;
            case MathCommands.PRIME_COMMAND:
                resume((ResultMessage<Boolean>) rawMessage, caches.getPrimeCache(), Boolean.class);
                break;
            default:
        }
    }

    public void aggregate(UUID primeId, Boolean primeResult) {
        caches.getPrimeMap().find(primeId).ifPresent(fibId ->
                caches.getFibMap().find(fibId).ifPresent(n ->
                        channels.find(n).ifPresent(channel ->
                                channel.offer(primeResult))));
    }

    private <R> void resume(ResultMessage<R> resultMessage, Cache<? extends Command<?, R>> cache, Class<R> resltType) {
        cache.find(resultMessage.getId()).ifPresent(cmd -> cmd.success(resultMessage));
    }

    public void execFibCommand(long n) {
        CommandMessage<Long> fibMsg = MathCommands.createFibCommand(n);
        FibCommand fibCmd = context.getBean(FibCommand.class);
        fibCmd.exec(fibMsg);
    }

    public void execPrimeCommand(long n) {
        CommandMessage<Long> primeMsg = MathCommands.createPrimeCommand(n);
        PrimeCommand primeCmd = context.getBean(PrimeCommand.class);
        primeCmd.exec(primeMsg);
    }
}
