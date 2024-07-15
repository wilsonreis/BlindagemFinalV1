package com.santander.kpv.utils;

import com.ibm.mq.jakarta.jms.MQDestination;
import com.ibm.msg.client.jakarta.wmq.WMQConstants;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import org.springframework.jms.support.destination.DestinationResolver;

public class ResolverUtils implements DestinationResolver {
    @Override
    public Destination resolveDestinationName(Session session, String dest, boolean pubSub) throws JMSException {
        Destination destination = session.createQueue(dest);

        MQDestination mqDestination = (MQDestination) destination;
        mqDestination.setTargetClient(WMQConstants.WMQ_CLIENT_NONJMS_MQ);
        return destination;
    }
}
