package com.taotao.sso.controller;

import com.alibaba.fastjson.JSON;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;
import com.taotao.sso.service.UserRegisterService;
import com.taotao.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Peng on 2018/11/22.
 */
@Controller
public class UserController {


    @Autowired
    private UserRegisterService userRegisterService;

    @Autowired
    private UserLoginService userLoginService;

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    /**
     * url:/user/check/{param}/{type}
     *
     * @param param
     * @param type 1  2  3  username,phone,email
     * @return
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkData(@PathVariable String param, @PathVariable Integer type){
        //1.引入服务
        //2.注入服务
        //3.调用服务
        TaotaoResult taotaoResult = userRegisterService.checkUserData(param, type);

        return taotaoResult;
    }


    /**
     * url:/user/register
     * 使用post
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser user){
        TaotaoResult result = userRegisterService.register(user);
        return result;
    }

    /**
     * url:/user/login
     *
     *提交方法：post
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username, String password, HttpServletRequest request,
                              HttpServletResponse response){
        TaotaoResult taotaoResult = userLoginService.login(username, password);
        //需要设置token到cookie中 可以使用 工具类  cookie需要跨域
        if (taotaoResult.getStatus()==200){
            CookieUtils.setCookie(request,response,TOKEN_KEY,taotaoResult.getData().toString());
        }
        return taotaoResult;
    }


    /**
     * url:/user/token/{token}
     * method:GET
     * @param token
     * @return
     */
    //第一种跨域解决方式，判断是否有参数callback，有，则返回构造的 callback（）js函数，否则返回json字符串
//    @RequestMapping(value = "/user/token/{token}",method = RequestMethod.GET)
//    @ResponseBody
//    public String getUserByToken(@PathVariable String token,String callback){
//        TaotaoResult result = userLoginService.getUserByToken(token);
//        if (StringUtils.isNotBlank(callback)){
//            return callback+"("+ JSON.toJSONString(result)+")";
//        }
//        return JSON.toJSONString(result);
//    }

    //第二种跨域解决方式，也是先判断是否有callback参数，但需要spring版本4.1以上，
    // 通过MappingJavksonValue的方法setJsonpFunction来设置callback返回函数，
    //返回类型改为 object
    @RequestMapping(value = "/user/token/{token}",method = RequestMethod.GET)
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callback){
        TaotaoResult result = userLoginService.getUserByToken(token);
        if (StringUtils.isNotBlank(callback)){
            MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }

    /**
     * url:/user/logout/{token}
     * 请求方法method:GET
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/logout/{token}",method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult logout(@PathVariable String token){
        TaotaoResult result = userLoginService.logout(token);
        return result;
    }
}
