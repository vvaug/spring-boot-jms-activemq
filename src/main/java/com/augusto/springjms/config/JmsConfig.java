package com.augusto.springjms.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class JmsConfig {

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	@Value("${spring.activemq.user}")
	private String user;

	@Value("${spring.activemq.password}")
	private String password;
	
	/* Bean to get a ActiveMQ ConnectionFactory */
	
	@Bean 
	public ActiveMQConnectionFactory getConnectionFactory() {
		if ("".equals(user)) {
			return new ActiveMQConnectionFactory(brokerUrl);
		}
		return new ActiveMQConnectionFactory(user, password, brokerUrl);
	}
	
	/*
	 * Configuring the listener (subscribing)
	 */
	
	@Bean
    public JmsListenerContainerFactory<?> jmsFactoryTopic(ConnectionFactory connectionFactory,
                                                          DefaultJmsListenerContainerFactoryConfigurer configurer) {
		/* A JmsListenerContainerFactory implementation to build a regular
         * DefaultMessageListenerContainer. 
         */
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		/*
		 * Configure the specified jms listener container factory.
		 * The factory can be further tuned and default settings can be overridden.
		 */
		configurer.configure(factory, connectionFactory);
        
		/*
         * we use the setPubSubDomain() method on the ListenerContainerFactory
         *  to specify that we want to use topics.
         */
		factory.setPubSubDomain(true);
        return factory;
    }
	/*
	 * Configuring the sender for queue
	 */
	@Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(getConnectionFactory());
    }
	/*
	 * Configuring the sender for topic
	 */    
	@Bean
    public JmsTemplate jmsTemplateTopic() {
        JmsTemplate jmsTemplate = new JmsTemplate(getConnectionFactory());
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }
}
