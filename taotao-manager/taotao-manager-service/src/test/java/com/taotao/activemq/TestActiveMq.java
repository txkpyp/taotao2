package com.taotao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * Created by Peng on 2018/11/15.
 */
public class TestActiveMq {

    //    测试ActiveMQ 的queue模式
    /*
    * 生产者
    * */
    @Test
    public void testQueueProducer() throws JMSException {
        //1.创建一个连接工程对象CollectionFactory对象。需要指定mq服务的ip和端口号，注意参数brokerURL的开头是
        //tcp:// 而不是http://,端口号是61616 而不是我们访问activemq后台管理页面所使用的8161
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.37.166:61616");
        //2.利用connectionFactory创建一个连接connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启connection
        connection.start();
        //4.利用connction 创建一个Session对象
        //第一个参数是 是否开启事务，一般不使用分布式事务，因为它特别耗性能，而且顾客体验极差，现在互联网
        //的做法是保证数据的最终一致（也就是允许数据暂时不一致），比如顾客买东西，一但订单生成完就立刻向用户响应下单成功。
        //至于下单后的一系列操作，比如通知会计记账，通知物流发货、商品数量同步等等先不要管，只需要发送一条消息到消息队列，
        //消息队列来告知各模块进行相应的操作，一次告知不行，就三次，直到完成相关的操作为止，这也做到了数据的最终一致性。
        //如果第一个参数为true，那么第二个参数将会被忽略。如果第一个参数为false，那么第二个参数为消息应答模式，常见的
        //有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用session对象创建一个Destination对象。有两种形式queue、topic，现在我们使用queue
        //参数就是消息队列的名称
        Queue queue = session.createQueue("test-queue");
        //6.使用session对象创建一个Producer对象。
        MessageProducer producer = session.createProducer(queue);
        //7.创建一个TextMessage对象，
        //有两种方式.第一种方式：
//        TextMessage textMessage=new ActiveMQTextMessage();
//        textMessage.setText("hello ,activeMQ");
        //第二种方式
        TextMessage textMessage = session.createTextMessage("hello,activeMQ2222!");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();

    }

    /*
    * queue模式的消费者
    * */
    @Test
    public void testQueueConsumer() throws Exception {
        //1.创建一个连接工程对象CollectionFactory对象。需要指定mq服务的ip和端口号，注意参数brokerURL的开头是
        //tcp:// 而不是http://,端口号是61616 而不是我们访问activemq后台管理页面所使用的8161
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.37.166:61616");
        //2.利用connectionFactory创建一个连接connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启connection
        connection.start();
        //4.利用connction 创建一个Session对象
        //第一个参数是 是否开启事务，一般不使用分布式事务，因为它特别耗性能，而且顾客体验极差，现在互联网
        //的做法是保证数据的最终一致（也就是允许数据暂时不一致），比如顾客买东西，一但订单生成完就立刻向用户响应下单成功。
        //至于下单后的一系列操作，比如通知会计记账，通知物流发货、商品数量同步等等先不要管，只需要发送一条消息到消息队列，
        //消息队列来告知各模块进行相应的操作，一次告知不行，就三次，直到完成相关的操作为止，这也做到了数据的最终一致性。
        //如果第一个参数为true，那么第二个参数将会被忽略。如果第一个参数为false，那么第二个参数为消息应答模式，常见的
        //有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用session对象创建一个Destination对象。有两种形式queue、topic，现在我们使用queue
        //参数就是消息队列的名称
        Queue queue = session.createQueue("test-queue");
        //6.使用session对象创建一个Consumer对象。
        MessageConsumer consumer = session.createConsumer(queue);
        //7.向consumer对象中设置一个MeassageListener对象，用来接收此消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //8.程序等待接收用户结束操作
        //程序不知道什么时候有消息，也不知道什么时候不再发送消息，这时就需要手动干预
        //当我们想停止接收消息时。可以往控制台输入任意键，然后回车即可停止接收操作（也可以直接回车）；
        System.in.read();
        //9.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

    //    测试ActiveMQ 的topic模式
    @Test
    public void testTopicProducer() throws JMSException {
        //1.创建一个连接工程对象CollectionFactory对象。需要指定mq服务的ip和端口号，注意参数brokerURL的开头是
        //tcp:// 而不是http://,端口号是61616 而不是我们访问activemq后台管理页面所使用的8161
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.37.166:61616");
        //2.利用connectionFactory创建一个连接connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启connection
        connection.start();
        //4.利用connction 创建一个Session对象
        //第一个参数是 是否开启事务，一般不使用分布式事务，因为它特别耗性能，而且顾客体验极差，现在互联网
        //的做法是保证数据的最终一致（也就是允许数据暂时不一致），比如顾客买东西，一但订单生成完就立刻向用户响应下单成功。
        //至于下单后的一系列操作，比如通知会计记账，通知物流发货、商品数量同步等等先不要管，只需要发送一条消息到消息队列，
        //消息队列来告知各模块进行相应的操作，一次告知不行，就三次，直到完成相关的操作为止，这也做到了数据的最终一致性。
        //如果第一个参数为true，那么第二个参数将会被忽略。如果第一个参数为false，那么第二个参数为消息应答模式，常见的
        //有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用session对象创建一个Destination对象。有两种形式queue、topic，我们这里使用topic
        //参数就是消息队列的名称
        Topic topic = session.createTopic("test-topic");
        //6.使用session对象创建一个Producer对象。
        MessageProducer producer = session.createProducer(topic);
        //7.创建一个TextMessage对象，
        //有两种方式.第一种方式：
//        TextMessage textMessage=new ActiveMQTextMessage();
//        textMessage.setText("hello ,activeMQ");
        //第二种方式
        TextMessage textMessage = session.createTextMessage("hello,activeMQ,topic方式222!");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    /*
    * topic模式的消费者
    * */
    @Test
    public void testTopicConsumer() throws Exception {
        //1.创建一个连接工程对象CollectionFactory对象。需要指定mq服务的ip和端口号，注意参数brokerURL的开头是
        //tcp:// 而不是http://,端口号是61616 而不是我们访问activemq后台管理页面所使用的8161
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.37.166:61616");
        //2.利用connectionFactory创建一个连接connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启connection
        connection.start();
        //4.利用connction 创建一个Session对象
        //第一个参数是 是否开启事务，一般不使用分布式事务，因为它特别耗性能，而且顾客体验极差，现在互联网
        //的做法是保证数据的最终一致（也就是允许数据暂时不一致），比如顾客买东西，一但订单生成完就立刻向用户响应下单成功。
        //至于下单后的一系列操作，比如通知会计记账，通知物流发货、商品数量同步等等先不要管，只需要发送一条消息到消息队列，
        //消息队列来告知各模块进行相应的操作，一次告知不行，就三次，直到完成相关的操作为止，这也做到了数据的最终一致性。
        //如果第一个参数为true，那么第二个参数将会被忽略。如果第一个参数为false，那么第二个参数为消息应答模式，常见的
        //有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用session对象创建一个Destination对象。有两种形式queue、topic，我们这使用topic
        //参数就是消息队列的名称
        Topic topic = session.createTopic("test-topic");
        //6.使用session创建一个consumer对象
        MessageConsumer consumer = session.createConsumer(topic);
        //7.向consumer中添加一个massageListener对象，用于接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //8.程序等待接收用户结束操作
        //程序不知道什么时候有消息，也不知道什么时候不再发送消息，这时就需要手动干预
        //当我们想停止接收消息时。可以往控制台输入任意键，然后回车即可停止接收操作（也可以直接回车）；
        System.out.println("topic消费者333。。。");
        System.in.read();
        //9.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }


    //    将topic消息持久化
    @Test
    public void testPersistenceTopicProducer() throws Exception {
        //1.创建一个连接工程对象CollectionFactory对象。需要指定mq服务的ip和端口号，注意参数brokerURL的开头是
        //tcp:// 而不是http://,端口号是61616 而不是我们访问activemq后台管理页面所使用的8161
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.37.166:61616");
        //2.设置使用异步发送消息，这样可以显著的提高发送的性能
        connectionFactory.setUseAsyncSend(true);
        //3.利用connectionFactory创建一个连接connection对象
        Connection connection = connectionFactory.createConnection();
        //4.设置一个生产者ID，对于每一个生产者来讲，其clientID都是唯一的
        connection.setClientID("producer2");
        //5.开启连接
        connection.start();
        //6.利用connction 创建一个Session对象
        //第一个参数是 是否开启事务，一般不使用分布式事务，因为它特别耗性能，而且顾客体验极差，现在互联网
        //的做法是保证数据的最终一致（也就是允许数据暂时不一致），比如顾客买东西，一但订单生成完就立刻向用户响应下单成功。
        //至于下单后的一系列操作，比如通知会计记账，通知物流发货、商品数量同步等等先不要管，只需要发送一条消息到消息队列，
        //消息队列来告知各模块进行相应的操作，一次告知不行，就三次，直到完成相关的操作为止，这也做到了数据的最终一致性。
        //如果第一个参数为true，那么第二个参数将会被忽略。如果第一个参数为false，那么第二个参数为消息应答模式，常见的
        //有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //7.使用session对象创建一个Destination对象。有两种形式queue、topic，我们这里使用topic
        //参数就是消息队列的名称
        Topic topic = session.createTopic("test-topic");
        //8.利用session创建一个Producer对像
        MessageProducer producer = session.createProducer(topic);
        //9.设置持久化消息，DeliveryMode设置为persistent（持久化）
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //10.创建一个TextMessage对象，
        //有两种方式.第一种方式：
//        TextMessage textMessage=new ActiveMQTextMessage();
//        textMessage.setText("hello ,activeMQ");
        //第二种方式
        TextMessage textMessage = session.createTextMessage("hello,activeMQ,topic持久化方式!666");
        //11.发送消息
        producer.send(textMessage);
        //12.关闭资源，连接
        producer.close();
        session.close();
        connection.close();
    }


    //    接收topic持久化消息的消费者
    @Test
    public void testTopicPersistenceConsumer() throws Exception {
        //1.创建一个连接工程对象CollectionFactory对象。需要指定mq服务的ip和端口号，注意参数brokerURL的开头是
        //tcp:// 而不是http://,端口号是61616 而不是我们访问activemq后台管理页面所使用的8161
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.37.166:61616");
        //2.设置使用异步接收消息，这样可以显著的提高发送的性能
        connectionFactory.setUseAsyncSend(true);
        //3.利用connectionFactory创建一个连接connection对象
        Connection connection = connectionFactory.createConnection();
        //4.设置消费者的ID，每个消费者的ClientID 都是唯一的
        connection.setClientID("consumer2");
        //5.开启连接
        connection.start();
        //6.利用connction 创建一个Session对象
        //第一个参数是 是否开启事务，一般不使用分布式事务，因为它特别耗性能，而且顾客体验极差，现在互联网
        //的做法是保证数据的最终一致（也就是允许数据暂时不一致），比如顾客买东西，一但订单生成完就立刻向用户响应下单成功。
        //至于下单后的一系列操作，比如通知会计记账，通知物流发货、商品数量同步等等先不要管，只需要发送一条消息到消息队列，
        //消息队列来告知各模块进行相应的操作，一次告知不行，就三次，直到完成相关的操作为止，这也做到了数据的最终一致性。
        //如果第一个参数为true，那么第二个参数将会被忽略。如果第一个参数为false，那么第二个参数为消息应答模式，常见的
        //有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //7.使用session对象创建一个Destination对象。有两种形式queue、topic，我们这里使用topic
        //参数就是消息队列的名称
        Topic topic = session.createTopic("test-topic");
        //8.利用session.createDurableSubscriber()方法创建一个consumer对象
        MessageConsumer consumer = session.createDurableSubscriber(topic, "consumer2");
        //9.向consumer中添加一个messageListener对象，用于接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //10.程序等待接收用户结束操作
        //程序不知道什么时候有消息，也不知道什么时候不再发送消息，这时就需要手动干预
        //当我们想停止接收消息时。可以往控制台输入任意键，然后回车即可停止接收操作（也可以直接回车）；
        System.out.println("topic持久化消费者222。。。");
        System.in.read();
        //9.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
