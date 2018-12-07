package com.taotao.service;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

/**
 * 商品服务接口
 * Created by Peng on 2018/11/6.
 */
public interface ItemService {

    //根据商品ID查询商品
    TbItem getItemById(long itemId);

    //查询商品列表
    public EasyUIDataGridResult getItemList(Integer page, Integer rows);

    //    添加商品
    public TaotaoResult createItem(TbItem item, String desc) throws Exception;

    //根据商品ID来查询商品描述
    TbItemDesc getItemDescById(Long itemId);
}
