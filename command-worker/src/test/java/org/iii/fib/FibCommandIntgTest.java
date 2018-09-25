package org.iii.fib;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.iii.common.CommandMessage;
import org.iii.common.ResultMessage;
import org.iii.mq.CommandExecutors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FibCommandIntgTest {

    private static final long N = 10;

    @SuppressWarnings("unchecked")
    @Test
    public void testFibCommand() throws InterruptedException, ExecutionException {
        CommandMessage<Long> commandMessage = prepareFibCommandMessage();
        ResultMessage<Long> resultMessage = (ResultMessage<Long>) CommandExecutors.executeCommand(commandMessage)
                                                                                  .get();
        Assert.assertEquals(55, resultMessage.getResult().longValue());
    }

    private CommandMessage<Long> prepareFibCommandMessage() {
        return CommandMessage.<Long>builder().id(UUID.randomUUID())
                                             .name(FibCommand.COMMAND_NAME)
                                             .param(N)
                                             .build();
    }
}
