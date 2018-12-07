package com.taotao.order.pojo;

import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

/**
 * 订单页面的实体综合类
 * Created by Peng on 2018/11/26.
 */
public class OrderInfo extends TbOrder implements Serializable{
    //订单商品实体的集合
    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
