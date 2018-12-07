package com.taotao.sso.service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

/**
 * Created by Peng on 2018/11/22.
 */
public interface UserRegisterService {
//    检查数据是否可用,data是参数值，type是参数类型
    public TaotaoResult checkUserData(String data,Integer type);

//    注册
    public TaotaoResult register(TbUser user);
}
