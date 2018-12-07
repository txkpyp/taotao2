package com.taotao.controller;

import com.taotao.content.service.ContentCategoryService;
import com.taotao.pojo.EasyUITreeCode;
import com.taotao.pojo.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 内容分类的处理Controller
 * Created by Peng on 2018/11/8.
 */
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;


    /**
     * url : '/content/category/list',
     animate: true,
     method : "GET",
     参数: id
     */
    @RequestMapping(value = "/content/category/list",method = RequestMethod.GET)
    @ResponseBody
    public List<EasyUITreeCode> getCOntentCategroyList(@RequestParam(value = "id",defaultValue = "0") long parentId){
        //1.引入服务
        //2.注入服务
        //3.调用服务的方法
        List<EasyUITreeCode> contentCategoryList = contentCategoryService.getContentCategoryList(parentId);
        return contentCategoryList;
    }


    // /content/category/create
    //method=post
    //参数：
    //parentId：就是新增节点的父节点的Id
    //name：新增节点的文本
    //返回值taotaoresult 包含分类的id
    /**
     * 添加节点
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createContentCategory(long parentId,String name){
        TaotaoResult taotaoResult = contentCategoryService.addContentCategroy(parentId, name);
        return taotaoResult;
    }


    //url:/content/category/update
    //method=post
    /**
     * 更新节点
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(value = "/content/category/update",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContentCategory(long id,String name){
        //1.引入服务
        //2.注入服务
        //3.调用服务的方法
        TaotaoResult taotaoResult = contentCategoryService.updateContentCategory(id, name);
        return taotaoResult;
    }

    //url:/content/category/delete/
    //method=post
    /**
     * 删除节点
     * @param id
     * @return
     */
    @RequestMapping(value = "/content/category/delete/",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContentCategory(long id){
        //1.引入服务
        //2.注入服务
        //3.调用服务的方法
        TaotaoResult taotaoResult = contentCategoryService.deleteContentCategory(id);
        return taotaoResult;
    }
}
