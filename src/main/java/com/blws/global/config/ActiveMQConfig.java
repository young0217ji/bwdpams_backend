package com.blws.global.config;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ErrorHandler;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;

@Slf4j
@Configuration
@EnableJms
@NoArgsConstructor
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Bean("ReceiverActiveMQConnectionFactory")
    public ActiveMQConnectionFactory ReceiverActiveMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);
        return activeMQConnectionFactory;
    }

    @Bean("JmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory JmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(ReceiverActiveMQConnectionFactory());
        factory.setExceptionListener(ExceptionListener());
        factory.setRecoveryInterval(3000L);
        factory.setAutoStartup(true);
        return factory;
    }

    @Bean
    public ExceptionListener ExceptionListener(){
        ExceptionListener exceptionListener = new ExceptionListener() {
            @Override
            public void onException(JMSException e) {
                log.error("onException===",e);
            }
        };

        return exceptionListener;
    }

    @Bean("JmsSession")
    public ActiveMQSession JmsSession() throws JMSException {
        ActiveMQConnection connection = (ActiveMQConnection) ReceiverActiveMQConnectionFactory().createConnection();
        connection.start();
        ActiveMQSession session = (ActiveMQSession) connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        return session;
    }

    @Bean("JmsTemplate")
    public JmsTemplate JmsTemplate() {
        return new JmsTemplate(ReceiverActiveMQConnectionFactory());
    }

}