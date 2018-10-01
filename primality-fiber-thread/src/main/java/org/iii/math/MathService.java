package org.iii.math;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iii.concurrent.ChannelRegistry;
import org.iii.domain.CommandMessage;
import org.iii.mq.CommandSender;

import static org.iii.util.JsonUtils.convertJsonFromObject;

@Service
public class MathService {

    @Autowired
    private CommandSender sender;

    @Autowired
    private ChannelRegistry channels;

    public LinkedBlockingQueue<Long> calFib(long n) {
        CommandMessage<Long> commandMessage = MathCommands.createFibCommand(n);
        LinkedBlockingQueue<Long> channel = channels.create(commandMessage.getId(), Long.class);

        try {
            sender.sendCommand(convertJsonFromObject(commandMessage));
            return channel;
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }

    public LinkedBlockingQueue<Boolean> calPrime(long n) {
        CommandMessage<Long> commandMessage = MathCommands.createPrimeCommand(n);
        LinkedBlockingQueue<Boolean> channel = channels.create(commandMessage.getId(), Boolean.class);

        try {
            sender.sendCommand(convertJsonFromObject(commandMessage));
            return channel;
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }
}
