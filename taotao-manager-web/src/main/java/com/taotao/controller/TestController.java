package com.taotao.controller;

import com.taotao.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Peng on 2018/11/6.
 */
@Controller
public class TestController {
    @Autowired
    private TestService service;

    @RequestMapping("/test/queryNow")
    @ResponseBody
    public String queryNow(){
        //1.引入服务
        //2.注入服务
        //3.调用服务
        //4.返回
        return service.queryNow();
    }
}
