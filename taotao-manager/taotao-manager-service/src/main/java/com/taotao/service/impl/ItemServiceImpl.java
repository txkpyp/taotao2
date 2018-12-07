package com.taotao.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.pojo.*;
import com.taotao.mapper.TbItemMapper;
import com.taotao.service.ItemService;
import com.taotao.util.IDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Peng on 2018/11/6.
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private JedisClient jedisClient;
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    @Value("${ITEM_EXPIRE}")
    private Integer ITEM_EXPIRE;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "itemAddTopic")
    private Destination destination;

    @Override
    public TbItem getItemById(long itemId) {

        try {
            //查询数据库之前先查询缓存
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
            if (!StringUtils.isBlank(json)){
                //把json转换成对象返回
                return JSON.parseObject(json,TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);

        try {
            //把查询结果添加到缓存
            jedisClient.set(ITEM_INFO+":"+itemId+":BASE",JSON.toJSONString(tbItem));
            //设置过期时间，提高缓存的利用率
            jedisClient.expire(ITEM_INFO+":"+itemId+":BASE",ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tbItem;
    }

    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //1.设置分页的信息 使用pagehelper
        if (page == null) page = 1;
        if (rows == null) rows = 30;
        PageHelper.startPage(page, rows);
        //2.注入mapper
        //3.创建example 对象 不需要设置查询条件
        TbItemExample example = new TbItemExample();
        //4.根据mapper调用查询所有数据的方法
        List<TbItem> list = itemMapper.selectByExample(example);
        //5.获取分页的信息
        PageInfo<TbItem> info = new PageInfo<>(list);
        //6.封装到EasyUIDataGridResult
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) info.getTotal());
        result.setRows(info.getList());
        //7.返回
        return result;
    }

    //创建一个商品
    @Override
    public TaotaoResult createItem(TbItem item, String desc) throws Exception {
        //1.生成商品id
        final long itemId = IDUtils.genItemId();
        //2.补全Item属性
        item.setId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        itemMapper.insert(item);
        //添加商品描述
        inserItemDesc(itemId, desc);

        //发送activemq消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(itemId + "");

                return textMessage;
            }
        });

        return TaotaoResult.ok();
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        try {
            //查询数据库之前先查询缓存
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
            if (!StringUtils.isBlank(json)){
                //把json转换成对象返回
                return JSON.parseObject(json,TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);

        try {
            //把查询结果添加到缓存
            jedisClient.set(ITEM_INFO+":"+itemId+":DESC",JSON.toJSONString(itemDesc));
            //设置过期时间，提高缓存的利用率
            jedisClient.expire(ITEM_INFO+":"+itemId+":DESC",ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemDesc;
    }

    private void inserItemDesc(Long itemId, String desc) {
        //创建一个商品描述对应的pojo
        TbItemDesc itemDesc = new TbItemDesc();
        //补全属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        //向商品描述表插入数据
        itemDescMapper.insert(itemDesc);
    }
}
