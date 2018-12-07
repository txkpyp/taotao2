package com.taotao.sso.service.impl;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 *
 * Created by Peng on 2018/11/22.
 */
@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    @Autowired
    private TbUserMapper userMapper;

    @Override
    public TaotaoResult checkUserData(String data, Integer type) {
        //注入mapper
        //设置查询条件
        TbUserExample example=new TbUserExample();
        Criteria criteria=example.createCriteria();
        //1.判断用户名是否可用
        if (type == 1){
            if (StringUtils.isEmpty(data)){
                TaotaoResult.ok(false);
            }
            criteria.andUsernameEqualTo(data);
        } else if (type == 2){
            //2.判断电话是否可用
            criteria.andPhoneEqualTo(data);
        } else if (type == 3){
            //3.判断邮箱是否可用
            criteria.andEmailEqualTo(data);
        } else {
            return TaotaoResult.build(400,"所传参数非法");
        }

        List<TbUser> list = userMapper.selectByExample(example);
        if (list !=null && list.size()>0){
            return TaotaoResult.ok(false);
        }

        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult register(TbUser user) {
        //检验数据的有效性
        //判断用户名不能为空
        if (StringUtils.isEmpty(user.getUsername())){
            TaotaoResult.build(400,"用户名不能为空!");
        }
        //校验用户名是否被注册了
        TaotaoResult taotaoResult = checkUserData(user.getUsername(), 1);
        if (!(Boolean) taotaoResult.getData()){
            return TaotaoResult.build(400,"用户名不能重复！");
        }
        //判断密码
        if (StringUtils.isEmpty(user.getPassword())){
            return TaotaoResult.build(400,"密码不能为空！");
        }
        //判断电话号码
        if (StringUtils.isNotBlank(user.getPhone())){
            //如果电话号码不为空，还需要判断电话号码是否重复，电话号码是不能重复
            TaotaoResult taotaoResult1 = checkUserData(user.getPhone(), 2);
            if (!(Boolean) taotaoResult.getData()){
                return TaotaoResult.build(400,"电话号码不能重复！");
            }
        }
        //判断邮箱
        if (StringUtils.isNotBlank(user.getEmail())){
            //如果电邮箱不为空，还需要判断邮箱是否重复，邮箱是不能重复
            TaotaoResult taotaoResult1 = checkUserData(user.getEmail(), 3);
            if (!(Boolean) taotaoResult.getData()){
                return TaotaoResult.build(400,"邮箱不能重复！");
            }
        }
        //填充剩余属性
        user.setCreated(new Date());
        user.setUpdated(new Date());

        //密码要进行md5加密。我们不用添加而外的包，用spring自带的包即可
        String md5Str= DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Str);
        //注册
        userMapper.insert(user);

        return TaotaoResult.ok();
    }
}
