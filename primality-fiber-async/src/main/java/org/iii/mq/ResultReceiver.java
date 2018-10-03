package org.iii.mq;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import org.iii.domain.ResultMessage;
import org.iii.math.MathResults;
import org.iii.math.MathService;

import static org.iii.PrimalityFiberConfig.MQ_LISTENER_CONTAINER_FACTORY_NAME;
import static org.iii.PrimalityFiberConfig.RESULT_QUEUE;

@Component
public class ResultReceiver {

    @Autowired
    private MathService service;

    @JmsListener(destination = RESULT_QUEUE,
            containerFactory = MQ_LISTENER_CONTAINER_FACTORY_NAME)
    public void receiveResult(String jsonMessage) {
        System.out.printf("receive result: %s%n", jsonMessage);
        try {
            ResultMessage<?> resultMessage = MathResults.convertResult(jsonMessage);
            service.resumeCommand(resultMessage);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
