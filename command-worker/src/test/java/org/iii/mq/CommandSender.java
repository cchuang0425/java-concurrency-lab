package org.iii.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import org.iii.CommandWorkerConfig;

@Component
public class CommandSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendCommand(String commandMessage) {
        jmsTemplate.convertAndSend(CommandWorkerConfig.COMMAND_QUEUE, commandMessage);
    }
}
