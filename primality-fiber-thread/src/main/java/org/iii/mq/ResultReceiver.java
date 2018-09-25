package org.iii.mq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static org.iii.PrimalityFiberConfig.RESULT_QUEUE;

@Component
public class ResultReceiver {

    @JmsListener(destination = RESULT_QUEUE,
            containerFactory = "mqListenerContainerFactory")
    public void receiveResult(String jsonMessage) {

    }
}
