package com.taotao.cart.service.impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.taotao.cart.service.CartService;
import com.taotao.jedis.service.JedisClient;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.noggit.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng on 2018/11/28.
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;

    @Value("${TT_CART_REDIS_PRE_KEY}")
    private String TT_CART_REDIS_PRE_KEY;

    //登录状态下添加购物车
    @Override
    public TaotaoResult addItemCart(TbItem tbItem, Integer num, Long userId) {
        //1.查询，可以根据key和 filed 来获取一个商品
        TbItem item = queryItemByItemIdAndUserId(tbItem.getId(), userId);
        //2.判断要添加的商品是否存在于列表中
        if(item!=null){
            //3.如果不为空
            item.setNum(item.getNum()+num);
            //图片取一张，
            //设置到redis中
            jedisClient.hset(TT_CART_REDIS_PRE_KEY+":"+userId,item.getId()+"", JsonUtils.objectToJson(item));
        }else{
            //4.如果不存在，直接添加到redis中
            //查询商品的数据（商品的名称，数量，图片。。。），调用商品的服务，直接从controller传进来
            tbItem.setNum(num);
            if (tbItem.getImage()!=null){
                tbItem.setImage(tbItem.getImage().split(",")[0]);
            }
            //设置到redis中
            jedisClient.hset(TT_CART_REDIS_PRE_KEY+":"+userId,tbItem.getId()+"",JsonUtils.objectToJson(tbItem));
        }
        return TaotaoResult.ok();
    }

    //从Redis缓存数据中获取购物车列表，使用hget命令
    private TbItem queryItemByItemIdAndUserId(Long itemId,Long userId){
        String str = jedisClient.hget(TT_CART_REDIS_PRE_KEY + ":" + userId + "", itemId + "");
        if(StringUtils.isNoneBlank(str)){
            TbItem tbItem = JsonUtils.jsonToPojo(str, TbItem.class);
            return tbItem;
        }
        return null;
    }

    @Override
    public List<TbItem> getCartList(Long userId) {
        Map<String, String> map = jedisClient.hgetAll(TT_CART_REDIS_PRE_KEY + ":" + userId + "");
        List<TbItem> list=new ArrayList<>();
        if (map!=null){
            for (Map.Entry<String,String> entry:map.entrySet()){
                String value=entry.getValue();//商品的json数据
                TbItem tbItem = JsonUtils.jsonToPojo(value, TbItem.class);
                list.add(tbItem);
            }
        }
        return list;
    }

    @Override
    public TaotaoResult updateItemCartByItemId(Long userId, Long itemId, Integer num) {
        //根据用户id和商品的id来获取商品的对象
        TbItem tbItem = queryItemByItemIdAndUserId(itemId, userId);
        //判断其是否存在
        if (tbItem!=null){
            //更新数量,此时的 num 已经是修改过后的传过来的值，无需叠加
            tbItem.setNum(num);
            //设置或redis中
            jedisClient.hset(TT_CART_REDIS_PRE_KEY+":"+userId,itemId+"",JsonUtils.objectToJson(tbItem));
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteItemCartByItemId(Long userId, Long itemId) {
        jedisClient.hdel(TT_CART_REDIS_PRE_KEY+":"+userId,itemId+"");
        return TaotaoResult.ok();
    }
}
