package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 展示我们的登录和注册页面
 * Created by Peng on 2018/11/23.
 */
@Controller
public class PageController {

    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }

    @RequestMapping("/page/login")
    public String showLogin(String url, Model model){
        System.out.println("url===="+url);
        model.addAttribute("redirect",url);
        return "login";
    }
}
