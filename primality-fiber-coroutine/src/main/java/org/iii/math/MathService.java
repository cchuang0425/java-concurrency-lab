package org.iii.math;

import java.io.IOException;

import co.paralleluniverse.strands.channels.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iii.common.ChannelRegistry;
import org.iii.domain.CommandMessage;
import org.iii.mq.CommandSender;

import static org.iii.util.JsonUtils.convertJsonFromObject;

@Service
public class MathService {

    @Autowired
    private ChannelRegistry channels;

    @Autowired
    private CommandSender sender;

    public Channel<Long> calFib(long n) {
        CommandMessage<Long> fibCmd = MathCommands.createFibCommand(n);
        Channel<Long> channel = channels.create(fibCmd.getId(), Long.class);

        try {
            sender.sendCommand(convertJsonFromObject(fibCmd));
            return channel;
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }

    public Channel<Boolean> calPrime(long n) {
        CommandMessage<Long> primeCmd = MathCommands.createPrimeCommand(n);
        Channel<Boolean> channel = channels.create(primeCmd.getId(), Boolean.class);

        try {
            sender.sendCommand(convertJsonFromObject(primeCmd));
            return channel;
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }
}
