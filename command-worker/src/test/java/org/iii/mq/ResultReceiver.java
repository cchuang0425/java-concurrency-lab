package org.iii.mq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import org.iii.CommandWorkerConfig;
import org.iii.common.Results;

@Component
public class ResultReceiver {

    @JmsListener(destination = CommandWorkerConfig.RESULT_QUEUE,
            containerFactory = "mqListenerContainerFactory")
    public void receiveResult(String jsonMessage) {
        Results.pipeToCommand(jsonMessage);
    }
}
