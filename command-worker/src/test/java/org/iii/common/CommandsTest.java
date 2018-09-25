package org.iii.common;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.iii.fib.FibCommand;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandsTest {

    private ObjectMapper jsonMapper;

    @Before
    public void setUp() {
        jsonMapper = new ObjectMapper().findAndRegisterModules();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertCommandMessage() throws IOException {
        CommandMessage<Long> origMessage = prepareFibCommandMessage();
        String jsonMessage = jsonMapper.writeValueAsString(origMessage);
        CommandMessage<Long> convMessage = (CommandMessage<Long>) Commands.convertCommandMessage(jsonMessage);
        Assert.assertEquals(origMessage, convMessage);
    }

    private CommandMessage<Long> prepareFibCommandMessage() {
        return CommandMessage.<Long>builder().id(UUID.randomUUID())
                                             .name(FibCommand.COMMAND_NAME)
                                             .param(10L)
                                             .build();
    }

    @Test
    public void testRunCommand() throws ExecutionException, InterruptedException {
        CommandMessage<Long> commandMessage = prepareFibCommandMessage();
        CompletableFuture<ResultMessage<Long>> fibResult = Commands.runCommandAsync(commandMessage);
        ResultMessage<Long> result = fibResult.get();
        Assert.assertEquals(55L, result.getResult().longValue());
    }
}
