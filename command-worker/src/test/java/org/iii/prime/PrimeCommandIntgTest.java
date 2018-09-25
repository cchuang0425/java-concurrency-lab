package org.iii.prime;

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
public class PrimeCommandIntgTest {

    private static final long N = 11;

    @SuppressWarnings("unchecked")
    @Test
    public void testPrime() throws ExecutionException, InterruptedException {
        CommandMessage<Long> commandMessage = preparePrimeCommandMessage();
        ResultMessage<Boolean> resultMessage = (ResultMessage<Boolean>) CommandExecutors.executeCommand(commandMessage)
                                                                                        .get();

        Assert.assertTrue(resultMessage.getResult());
    }

    private CommandMessage<Long> preparePrimeCommandMessage() {
        return CommandMessage.<Long>builder().id(UUID.randomUUID())
                                             .name(PrimeCommand.COMMAND_NAME)
                                             .param(N)
                                             .build();
    }
}
