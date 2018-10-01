package org.iii.mq;

import java.io.IOException;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import org.iii.domain.ResultMessage;
import org.iii.math.MathResults;

import static org.iii.PrimalityFiberConfig.MQ_LISTENER_CONTAINER_FACTORY_NAME;
import static org.iii.PrimalityFiberConfig.RESULT_QUEUE;

@Component
public class ResultReceiver {

    @JmsListener(destination = RESULT_QUEUE,
            containerFactory = MQ_LISTENER_CONTAINER_FACTORY_NAME)
    public void receiveResult(String jsonMessage) {
        try {
            ResultMessage<?> resultMessage = MathResults.convertResult(jsonMessage);
            MathResults.resumeToCommand(resultMessage);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
