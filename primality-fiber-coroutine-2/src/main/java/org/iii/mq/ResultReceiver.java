package org.iii.mq;

import java.io.IOException;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import org.iii.PrimalityFiberConfig;
import org.iii.domain.ResultMessage;
import org.iii.math.MathResults;

import static org.iii.PrimalityFiberConfig.MQ_HOST;
import static org.iii.PrimalityFiberConfig.MQ_PATTERN;
import static org.iii.PrimalityFiberConfig.MQ_PORT;
import static org.iii.PrimalityFiberConfig.MQ_WAIT;
import static org.iii.PrimalityFiberConfig.RESULT_QUEUE;
import static org.iii.PrimalityFiberConfig.STATE_RUN;

public class ResultReceiver implements Runnable, ExceptionListener {

    private static ResultReceiver instance;

    public static ResultReceiver getInstance() {
        if (instance == null) {
            instance = new ResultReceiver();
        }

        return instance;
    }

    private ActiveMQConnectionFactory connFactory;

    private ResultReceiver() {
        String url = String.format(MQ_PATTERN, MQ_HOST, MQ_PORT);
        connFactory = new ActiveMQConnectionFactory(url);
    }

    @Override
    public void run() {
        try {
            Connection conn = connFactory.createConnection();
            conn.start();

            conn.setExceptionListener(this);

            Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination dest = sess.createQueue(RESULT_QUEUE);

            MessageConsumer consumer = sess.createConsumer(dest);

            while (PrimalityFiberConfig.getState() == STATE_RUN) {
                Message message = consumer.receive(MQ_WAIT);

                if (message instanceof TextMessage) {
                    TextMessage txtMsg = (TextMessage) message;
                    String jsonMessage = txtMsg.getText();
                    ResultMessage<?> resultMessage = MathResults.convertResult(jsonMessage);
                    MathResults.resumeToCommand(resultMessage);
                }
            }

            consumer.close();
            sess.close();
            conn.close();
        } catch (JMSException | IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void onException(JMSException exception) {

    }
}
