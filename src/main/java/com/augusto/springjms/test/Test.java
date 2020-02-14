package com.augusto.springjms.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Test implements ApplicationRunner {

	@Autowired private JmsTemplate jmsTemplate;
    @Autowired private JmsTemplate jmsTemplateTopic;


    @JmsListener(destination = "queue.financeiro", selector = "venda=true")
    public void onReceiverQueue(String str) {
        System.out.println("Received message on Queue: " + str );
    }
    
    /* broadcast */
    @JmsListener(destination = "topic.loja", containerFactory = "jmsFactoryTopic")
    public void onReceiverTopic(String str) {
        System.out.println("Client 1 Received message on Topic: " + str);
    }
    @JmsListener(destination = "topic.loja", containerFactory = "jmsFactoryTopic")
    public void onReceiverTopic2(String str) {
        System.out.println("Client 2 Received message on Topic: " + str);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        
    	jmsTemplate.convertAndSend("queue.financeiro", "{user: 'vic', usando: 'fila'}",
    			
    								x -> {
    										x.setBooleanProperty("venda", true);
    										return x;
    									}
    							  );
    	
        jmsTemplateTopic.convertAndSend("topic.loja", "{user: 'vic', usando: 'topico'}");
    
    }

}
