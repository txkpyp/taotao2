package com.taotao.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Peng on 2018/11/15.
 */
public class TestSpringActivemq {

    @Test
    public void testActveConsumer() throws Exception {
        //初始化spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //等待接收消息
        System.in.read();
    }
}
