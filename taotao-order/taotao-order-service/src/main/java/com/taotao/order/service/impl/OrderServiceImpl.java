package com.taotao.order.service.impl;

import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 生成订单服务
 * Created by Peng on 2018/11/26.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderShippingMapper shippingMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;
    @Value("${ORDER_ID_INIT_VALUE}")
    private String ORDER_ID_INIT_VALUE;
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;

    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        //1，插入订单表
        //生成订单号，可以使用redis的incr方法来生成，那就需要注入jedis的依赖
        //判断key没有存在，则初始化一个key设置一个初始值
        if (jedisClient.exists(ORDER_ID_GEN_KEY)){
            //设置初始值
            jedisClient.set(ORDER_ID_GEN_KEY,ORDER_ID_INIT_VALUE);
        }
        String orderId=jedisClient.incr(ORDER_ID_GEN_KEY).toString();
        //补全其他属性
        orderInfo.setOrderId(orderId);
        //付款状态，1，未付款，2.已付款，3.未发货，4.已发货，5，交易成功，6。交易关闭，刚刚开始肯定是未付款
        orderInfo.setStatus(1);
        //邮费
        orderInfo.setPostFee("0");
        //订单创建时间
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //向订单表中插入数据，由于orderinfo继承自Tborder，所以可以以orderinfo作为参数
        orderMapper.insert(orderInfo);
        //2.向订单明细表插入数据
        List<TbOrderItem> orderItemList = orderInfo.getOrderItems();
        for (TbOrderItem orderItem:orderItemList){
            //获得明细表的主键，第一次使用ORDER_ITEM_ID_GEN_KEY这个key，是没有初始值的，那么会自动初始值变为1
            String orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
            //补全属性，之所以只补全两个属性，因为itemID已经有了
            orderItem.setId(orderItemId);//这是订单明细表的主键
            orderItem.setOrderId(orderId);
            //插入明细表
            orderItemMapper.insert(orderItem);
        }
        //3.插入订单物流表
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        //设置订单id,补全其他属性
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        //插入
        shippingMapper.insert(orderShipping);
        //返回需要包含订单号
        return TaotaoResult.ok(orderId);
    }
}
