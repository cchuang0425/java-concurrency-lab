package org.iii.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import org.iii.CommandWorkerConfig;

@Component
public class ResultSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendResult(String resultMessage) {
        jmsTemplate.convertAndSend(CommandWorkerConfig.RESULT_QUEUE, resultMessage);
    }
}
