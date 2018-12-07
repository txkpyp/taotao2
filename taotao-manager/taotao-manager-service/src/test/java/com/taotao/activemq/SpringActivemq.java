package com.taotao.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * Created by Peng on 2018/11/15.
 */
public class SpringActivemq {


    @Test
    public void testSpringActivemq() throws Exception{
        //1，初始化一个spring容器，
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //2.从容器中取得JMSTemplate对象
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        //3.从容器中拿到一个Destination对象
        Queue queue= (Queue) context.getBean("queueDestination");
        //4.使用JMSDestination对象发送消息，需要知道Destination
        jmsTemplate.send(queue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage("spring activemq test1111");
                return message;
            }
        });

    }
}
