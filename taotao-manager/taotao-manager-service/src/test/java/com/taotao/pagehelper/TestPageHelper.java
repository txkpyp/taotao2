package com.taotao.pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peng on 2018/11/6.
 */
public class TestPageHelper {

    @Test
    public void testPageHelper(){

        //初始化Spring容器
        ApplicationContext applicationContext=
                new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        //获得mapper的代理对象
        TbItemMapper mapper=applicationContext.getBean(TbItemMapper.class);
        //设置分页信息
        PageHelper.startPage(1,30);
        //执行查询
        TbItemExample example=new TbItemExample();
        List<TbItem> list=mapper.selectByExample(example);
        //取分页信息
        PageInfo<TbItem> pageInfo=new PageInfo<TbItem>(list);
        System.out.println(pageInfo.getTotal());
        System.out.println(pageInfo.getPageNum());
        System.out.println(pageInfo.getPageSize());
        System.out.println(pageInfo.getPages());
    }

}
