package com.taotao.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.jedis.service.JedisClient;
import com.taotao.sso.service.UserLoginService;
import com.taotao.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * Created by Peng on 2018/11/22.
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient client;

    @Value("${USER_INFO}")
    private String USER_INFO;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public TaotaoResult login(String username, String password) {
        //注入mapper
        //1，校验账户密码是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return TaotaoResult.build(400, "用户名或密码错误！");
        }
        //2.先校验用户名
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = userMapper.selectByExample(example);//select*　ｆｒｏｍ　ｔｂｕｓｅｒ　ｗｈｅｒｅ　ｕｓｅｒｎａｍｅ＝１２３
        if(list==null || list.size()==0){
            return TaotaoResult.build(400,"用户名或密码错误!");
        }
        //3.再校验密码
        TbUser tbUser = list.get(0);//用户名是唯一的
        //先加密在校验
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!md5DigestAsHex.equals(tbUser.getPassword())) {//表示用户密码不正确
            return TaotaoResult.build(400, "用户名或密码错误！");
        }
        //4.如果校验成功
        //5.生成token :uuid, 还需要设置token的有效期来模拟session，用户的数据保存在Redis（key:token,value:用户的数据JSON）
        String token = UUID.randomUUID().toString();
        //注入JedisClient
        //存放用户数据到Redis中，使用jedis的客户端，为了管理方便，添加一个前缀 “kkk:token”
        //设置密码为空
        tbUser.setPassword(null);
        client.set(USER_INFO + ":" + token, JsonUtils.objectToJson(tbUser));
        //6.设置过期时间
        client.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        //7.把token设置到token当中，在表现层设置
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        String json = client.get(USER_INFO + ":" + token);
        //1.判断token已是否过期
        if(StringUtils.isBlank(json)){
            return TaotaoResult.build(400,"token已过期！");
        }

        //如果我们直接返回json的话，由于字符串中的 “ 在redis中是有特殊意义的，因此
        // ” 会被转义，这不是我们想要的结果，我们想要的结果是不带转义字符的字符串，因此我们
        // 需要把json转成对象，然后吧对象返回

        //2.把json转成对象
        TbUser tbUser = JSON.parseObject(json, TbUser.class);

        //我们每次访问一次该token，如果该token没有过期，我们需要更新token的时间值，在把token恢复
        //到原来的值

        //3，设置token的时间
        client.expire(USER_INFO + ":" + token,SESSION_EXPIRE);
        //4，返回结果
        return TaotaoResult.ok(tbUser);
    }

    @Override
    public TaotaoResult logout(String token) {
        //设置rendis中的expire值为零即可
        client.expire(USER_INFO + ":" + token,0);
        return TaotaoResult.ok();
    }
}
