package com.taotao.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peng on 2018/11/16.
 */

@Controller
public class HtmlGenController {


    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genHtml")
    @ResponseBody
    public String genHtml() throws Exception{
        //1，注入FreeMarkerConfigurer,根据config  获取configuration对象
        //生成静态页面
        Configuration configuration=freeMarkerConfigurer.getConfiguration();
        //2.设置模板文件 加载模板文件 /WEB-INF/ftl/相对路径
        Template template = configuration.getTemplate("hello.ftl");
        //3.创建数据集  --》从数据库中获取
        Map data=new HashMap();
        data.put("hello","spring freemarker test");
        //4.创建writer
        Writer out=new FileWriter(new File("K:/freemarker/out/springFreemarker.html"));
        //5.调用方法输出
        template.process(data,out);
        //6.关闭流
        out.close();
        //返回结果
        return "ok";
    }
}
