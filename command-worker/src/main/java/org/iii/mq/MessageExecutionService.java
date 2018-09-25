package org.iii.mq;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.iii.common.CommandMessage;
import org.iii.common.Commands;
import org.iii.common.ResultMessage;

import static org.iii.util.JsonUtils.jsonMapper;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Service
public class MessageExecutionService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    @Qualifier("workerPool")
    private Executor pool;

    @Async
    public void executeMessage(String jsonMessage) {
        CompletableFuture.runAsync(context.getBean(MessageRunner.class, jsonMessage), pool);
    }

    @Component
    @Scope(SCOPE_PROTOTYPE)
    public static class MessageRunner implements Runnable {

        @Autowired
        private ResultSender sender;

        private String jsonMessage;

        public MessageRunner(String jsonMessage) {
            this.jsonMessage = jsonMessage;
        }

        @Override
        public void run() {
            try {
                CommandMessage<?> commandMessage = Commands.convertCommandMessage(jsonMessage);
                ResultMessage<?> resultMessage = Commands.runCommandSync(commandMessage);
                sender.sendResult(jsonMapper().writeValueAsString(resultMessage));
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
}
