package com.santander.kpv.config;


import com.ibm.mq.jakarta.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.santander.kpv.exceptions.MyRuntimeException;
import com.santander.kpv.utils.ResolverUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
@Slf4j
public class BlindagemFinalConfiguration {

    @Value("${ibm.mq.host}")
    private String mqHostName;

    @Value("${ibm.mq.port}")
    private int mqPort;

    @Value("${ibm.mq.queueManager}")
    private String mqQueueManager;

    @Value("${ibm.mq.channel}")
    private String mqChannel;

    @Value("${ibm.mq.user}")
    private String user;
    @Value("${ibm.mq.password}")
    private String password;
    @Value("${ibm.mq.queueRequest}")
    private String queueRequest;

    @Value("${ibm.mq.queueResponse}")
    private String queueResponse;
    @Value("${ibm.mq.jmsExpiration}")
    private long jmsExpiration;

    public String getQueueRequest() {
        return queueRequest;
    }

    public String getQueueResponse() {
        return queueResponse;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getMqHostName() {
        return mqHostName;
    }

    public int getMqPort() {
        return mqPort;
    }

    public String getMqQueueManager() {
        return mqQueueManager;
    }

    public String getMqChannel() {
        return mqChannel;
    }

    public long getJmsExpiration() {
        return jmsExpiration;
    }

    @Bean("mqQueueConnectionFactory")
    public MQQueueConnectionFactory mqQueueConnectionFactory() {
        MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
        try {
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, getMqHostName());
            cf.setIntProperty(WMQConstants.WMQ_PORT, getMqPort());
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, getMqChannel());
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, 1);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, getMqQueueManager());
            cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "KPV.BLINDAGEM.FINAL");
            cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
            cf.setStringProperty(WMQConstants.USERID, getUser());
            cf.setStringProperty(WMQConstants.PASSWORD, getPassword());
            return cf;
        } catch (Exception e) {
            log.error("MQQueueConnectionFactory mqQueueConnectionFactory() {} ", e.getMessage());
            throw new MyRuntimeException("Error creating MQ connection factory", e);
        }
    }

    @Bean("nonJmsTemplate")
    public JmsTemplate nonJmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(mqQueueConnectionFactory());
        jmsTemplate.setDestinationResolver(new ResolverUtils());
        jmsTemplate.setReceiveTimeout(getJmsExpiration());
        return jmsTemplate;
    }
}