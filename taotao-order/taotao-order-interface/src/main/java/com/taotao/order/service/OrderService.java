package com.taotao.order.service;

import com.taotao.order.pojo.OrderInfo;
import com.taotao.pojo.TaotaoResult;

/**
 * Created by Peng on 2018/11/26.
 */
public interface OrderService {

    //生成订单，orderinfo包含了表单提交的所有信息
    public TaotaoResult createOrder(OrderInfo orderInfo);
}
