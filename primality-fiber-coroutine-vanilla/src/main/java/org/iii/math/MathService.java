package org.iii.math;

import java.io.IOException;

import co.paralleluniverse.strands.channels.Channel;

import org.iii.common.ChannelRegistry;
import org.iii.domain.CommandMessage;
import org.iii.mq.CommandSender;

import static org.iii.util.JsonUtils.convertJsonFromObject;

public class MathService {

    private static MathService instance;

    public static MathService getInstance() {
        if (instance == null) {
            instance = new MathService();
        }

        return instance;
    }

    private MathService() {}

    public Channel<Long> calFib(long n) {
        CommandMessage<Long> fibCmd = MathCommands.createFibCommand(n);
        Channel<Long> channel = ChannelRegistry.getInstance().create(fibCmd.getId(), Long.class);

        try {
            CommandSender.getInstance().sendCommand(convertJsonFromObject(fibCmd));
            return channel;
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }

    public Channel<Boolean> calPrime(long n) {
        CommandMessage<Long> primeCmd = MathCommands.createPrimeCommand(n);
        Channel<Boolean> channel = ChannelRegistry.getInstance().create(primeCmd.getId(), Boolean.class);

        try {
            CommandSender.getInstance().sendCommand(convertJsonFromObject(primeCmd));
            return channel;
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }
}
