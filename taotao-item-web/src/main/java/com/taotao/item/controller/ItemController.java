package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * 商品详情页面展示Controller
 * Created by Peng on 2018/11/16.
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItem(@PathVariable long itemId, Model model){
        //1，引入服务
        //注入服务
        //调用服务
        //获取商品基本信息
        TbItem tbItem = itemService.getItemById(itemId);
        Item item=new Item(tbItem);

        //获取商品描述
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);

        //返回给页面需要的对象
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",tbItemDesc);
        //返回逻辑视图
        return "item";
    }
}
