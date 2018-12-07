package com.taotao.controller;

import com.taotao.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Peng on 2018/11/13.
 */
@Controller
public class IndexManagerController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/index/import")
    @ResponseBody
    public TaotaoResult importIndex(){
        //引入服务
        //注入服务
        //调用服务
        TaotaoResult result = searchItemService.importItemToIndex();
        return result;
    }
}
