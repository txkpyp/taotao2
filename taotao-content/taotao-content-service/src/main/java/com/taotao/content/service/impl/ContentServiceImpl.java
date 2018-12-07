package com.taotao.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.data.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Peng on 2018/11/9.
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;

    @Override
    public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
        //1.注入mapper
        //设置分页信息
        PageHelper.startPage(page,rows);
        //执行查询
        TbContentExample example=new TbContentExample();
        Criteria criteria=example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //获取查询结果
        List<TbContent> list=contentMapper.selectByExample(example);
        PageInfo<TbContent> info=new PageInfo<TbContent>(list);
        EasyUIDataGridResult result=new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal((int)info.getTotal());
        //返回结果
        return result;
    }

    @Override
    public TaotaoResult addContent(TbContent content) {
        //补充属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //添加
        contentMapper.insertSelective(content);
        try {
            //同步缓存由于首页大广告位的分类ID为89，content.getCategoryId()得到的便是89
            jedisClient.hdel(INDEX_CONTENT,content.getCategoryId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回结果
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateContent(TbContent content) {
        //填充属性
        content.setUpdated(new Date());
        //更新内容
        contentMapper.updateByPrimaryKey(content);
        try {
            //同步缓存由于首页大广告位的分类ID为89，content.getCategoryId()得到的便是89
            jedisClient.hdel(INDEX_CONTENT,content.getCategoryId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContent(String ids) {
        String[] idList=ids.split(",");
        for (String id:idList){
            //删除内容
            contentMapper.deleteByPrimaryKey(Long.valueOf(id));
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult getContent(long id) {
        TbContent tbContent = contentMapper.selectByPrimaryKey(id);
        return TaotaoResult.ok(tbContent);
    }

    @Override
    public List<TbContent> getContentListByCid(long categoryId) {

        try {
            //首先查询缓存，如果存在缓存的话，就直接将结果返回前台展示，查询缓存不能正常业务流程
            String json=jedisClient.hget(INDEX_CONTENT,categoryId+"");
            //如果从缓存中查询到结果
            if (StringUtils.isNotBlank(json)){
                //将json转成List<TbContent>
                List<TbContent> list= JSON.parseArray(json,TbContent.class);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = contentMapper.selectByExample(example);
        //添加缓存
        try {
            String json=JSON.toJSONString(list);
            jedisClient.hset(INDEX_CONTENT,categoryId+"",json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return list;
    }
}
