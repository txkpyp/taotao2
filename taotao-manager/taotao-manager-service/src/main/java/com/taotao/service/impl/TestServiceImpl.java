package com.taotao.service.impl;

import com.taotao.mapper.TestMapper;
import com.taotao.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * 查询数据库当前时间的服务
 * Created by Peng on 2018/11/6.
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper mapper;
    @Override
    public String queryNow() {
        //1.注入mapper
        //2.调用mapper的方法
        return mapper.queryNow();
    }
}
