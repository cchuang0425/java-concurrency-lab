package org.iii.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import org.iii.CommandWorkerConfig;

@Component
public class CommandReceiver {

    @Autowired
    private MessageExecutionService executor;

    @JmsListener(destination = CommandWorkerConfig.COMMAND_QUEUE,
            containerFactory = "mqListenerContainerFactory")
    public void receiveCommand(String commandMessage) {
        executor.executeMessage(commandMessage);
    }
}
