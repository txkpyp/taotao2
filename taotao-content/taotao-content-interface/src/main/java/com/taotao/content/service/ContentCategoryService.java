package com.taotao.content.service;

import com.taotao.pojo.EasyUITreeCode;
import com.taotao.pojo.TaotaoResult;

import java.util.List;

/**
 * Created by Peng on 2018/11/8.
 */
public interface ContentCategoryService {
    //获取内容分类列表
    List<EasyUITreeCode> getContentCategoryList(long parentId);
    //添加内容分类
    TaotaoResult addContentCategroy(long parentId,String name);
    //修改内容分类，注意参数名称要与content-category.jsp页面指定的参数名称一致
    TaotaoResult updateContentCategory(long id,String name);
    //删除内容分类，注意参数名称要与content-category.jsp页面指定的参数名称一致
    TaotaoResult deleteContentCategory(long id);
}
