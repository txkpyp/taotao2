package com.taotao.sso.service;

import com.taotao.pojo.TaotaoResult;

/**
 * Created by Peng on 2018/11/22.
 */
public interface UserLoginService {
    //登录
    public TaotaoResult login(String username,String password);

    //根据token获取用户信息
    public TaotaoResult getUserByToken(String token);

    //退出登录
    public TaotaoResult logout(String token);
}
