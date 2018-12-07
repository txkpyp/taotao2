package com.taotao.content.service;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * 内容列表管理
 * Created by Peng on 2018/11/9.
 */
public interface ContentService {
    //获取内容列表
    public EasyUIDataGridResult getContentList(long categoryId,int page,int rows);
    //添加内容
    public TaotaoResult addContent(TbContent content);
    public TaotaoResult updateContent(TbContent content);
    public TaotaoResult deleteContent(String ids);
    //获取单个内容信息
    TaotaoResult getContent(long id);

    //根据内容分类ID来获取内容列表
    List<TbContent> getContentListByCid(long categoryId);
}
