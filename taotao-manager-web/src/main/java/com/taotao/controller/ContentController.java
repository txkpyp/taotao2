package com.taotao.controller;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Peng on 2018/11/9.
 */
@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult getContentList(long categoryId,int page,int rows){
        //引入服务
        //注入服务
        //调用服务
        EasyUIDataGridResult result = contentService.getContentList(categoryId, page, rows);
        return result;
    }

    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent content){
        TaotaoResult result = contentService.addContent(content);
        return result;
    }

    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public TaotaoResult editContent(TbContent content){
        TaotaoResult result = contentService.updateContent(content);
        return result;
    }

    @RequestMapping("/content/delete")
    @ResponseBody
    public TaotaoResult deleteContent(String ids){
        TaotaoResult result = contentService.deleteContent(ids);
        return result;
    }

    @RequestMapping("/content/getContent")
    @ResponseBody
    public TaotaoResult getContent(long id){
        TaotaoResult result = contentService.getContent(id);
        return result;
    }
}
