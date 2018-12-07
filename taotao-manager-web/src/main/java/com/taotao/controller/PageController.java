package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 展示后台首页控制器
 * Created by Peng on 2018/11/6.
 */
@Controller
public class PageController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page) {
        return page;
    }

}
