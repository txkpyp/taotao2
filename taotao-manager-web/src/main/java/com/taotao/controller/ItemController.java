package com.taotao.controller;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Peng on 2018/11/6.
 */
@Controller
public class ItemController {


    @Autowired
    private ItemService itemService;

    //url:/item/list
    //method:get
    //参数：page,rows
    //返回值:json
    @RequestMapping(value = "/item/list",method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows){

        //1.引入服务
        //2.注入服务
        //3.调用服务
        return itemService.getItemList(page,rows);
    }


    //url:/item/save
    //method:post
    //参数：TbItem,desc
    //返回值:TaotaoResult
    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createItem(TbItem item,String desc){
        try {
            TaotaoResult taotaoResult = itemService.createItem(item, desc);
            return taotaoResult;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,"添加失败！");
        }
    }

}
