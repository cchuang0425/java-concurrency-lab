package org.iii.mq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import static org.iii.PrimalityFiberConfig.COMMAND_QUEUE;
import static org.iii.PrimalityFiberConfig.MQ_HOST;
import static org.iii.PrimalityFiberConfig.MQ_PATTERN;
import static org.iii.PrimalityFiberConfig.MQ_PORT;

public class CommandSender {
    private static CommandSender instance;

    public static CommandSender getInstance() {
        if (instance == null) {
            instance = new CommandSender();
        }

        return instance;
    }

    private ActiveMQConnectionFactory connFactory;

    private CommandSender() {
        String url = String.format(MQ_PATTERN, MQ_HOST, MQ_PORT);
        connFactory = new ActiveMQConnectionFactory(url);
    }

    public void sendCommand(String jsonMessage) {
        CommandSender.sendMessage(connFactory, COMMAND_QUEUE, jsonMessage);
    }

    private static void sendMessage(ActiveMQConnectionFactory connFactory, String queue, String jsonMessage) {
        try {
            Connection conn = connFactory.createConnection();
            conn.start();

            Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination dest = sess.createQueue(queue);

            MessageProducer producer = sess.createProducer(dest);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage message = sess.createTextMessage(jsonMessage);

            producer.send(message);

            sess.close();
            conn.close();
        } catch (JMSException e) {
            e.printStackTrace(System.err);
        }
    }
}
