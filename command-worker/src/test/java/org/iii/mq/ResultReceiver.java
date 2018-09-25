package org.iii.mq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import org.iii.common.Results;

import static org.iii.CommandWorkerConfig.MQ_LISTENER_CONTAINER_FACTORY_NAME;
import static org.iii.CommandWorkerConfig.RESULT_QUEUE;

@Component
public class ResultReceiver {

    @JmsListener(destination = RESULT_QUEUE,
            containerFactory = MQ_LISTENER_CONTAINER_FACTORY_NAME)
    public void receiveResult(String jsonMessage) {
        Results.pipeToCommand(jsonMessage);
    }
}
