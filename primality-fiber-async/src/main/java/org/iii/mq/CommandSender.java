package org.iii.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static org.iii.PrimalityFiberConfig.COMMAND_QUEUE;

@Component
public class CommandSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendCommand(String jsonMessage) {
        System.out.printf("send command: %s%n", jsonMessage);
        jmsTemplate.convertAndSend(COMMAND_QUEUE, jsonMessage);
    }
}
