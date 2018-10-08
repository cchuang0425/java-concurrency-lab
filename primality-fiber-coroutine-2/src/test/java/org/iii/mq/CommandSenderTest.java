package org.iii.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import org.iii.domain.CommandMessage;
import org.iii.math.MathCommands;
import org.iii.util.JsonUtils;

public class CommandSenderTest {
    @Test
    public void testSendCommand() throws JsonProcessingException {
        CommandMessage<Long> fibCmd = MathCommands.createFibCommand(1);
        CommandSender.getInstance().sendCommand(JsonUtils.convertJsonFromObject(fibCmd));
    }
}
