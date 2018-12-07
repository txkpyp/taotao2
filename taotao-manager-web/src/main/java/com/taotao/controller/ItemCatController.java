package com.taotao.controller;

import com.taotao.pojo.EasyUITreeCode;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Peng on 2018/11/7.
 */
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;
    /**
     * url:/item/cat/list
     * 参数：id 初始化默认给一个0
     * @param parentId
     * @return
     */
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeCode> getItemCatList(@RequestParam(name = "id",defaultValue = "0") long parentId){
        //1.引入服务
        //2.注入服务
        //3.调用服务
        List<EasyUITreeCode> itemCatList = itemCatService.getItemCatList(parentId);
        return itemCatList;
    }
}
