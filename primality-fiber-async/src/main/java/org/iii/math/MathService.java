package org.iii.math;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iii.domain.CommandMessage;
import org.iii.mq.CommandSender;

import static org.iii.util.JsonUtils.convertJsonFromObject;

@Service
public class MathService {

    @Autowired
    private CommandSender sender;

    public void sendFib(long n) {
        CommandMessage<Long> fibCmd = MathCommands.createFibCommand(n);

        try {
            sender.sendCommand(convertJsonFromObject(fibCmd));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
