package com.taotao.order.interceptor;

import com.taotao.pojo.TaotaoResult;
import com.taotao.sso.service.UserLoginService;
import com.taotao.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 登录拦截器
 * Created by Peng on 2018/11/25.
 */
public class LoginInterceptor implements HandlerInterceptor{

    @Autowired
    private UserLoginService userLoginService;

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @Value("${SSO_URL}")
    private String SSO_URL;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //执行时机，在handler之前执行此方法，拦截请求让用户登录就在这个方法拦截
        //1.从cookie中获取token
        String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
        //2.如果获取不到key，就跳转到sso单点登录页面，需要把当前请求的url作为参数传给sso，sso登录成功之后跳转回请求的页面
        if (StringUtils.isEmpty(token)){
            //取得当前请求的url
            String requestURL=request.getRequestURL().toString();
            //跳转到登录页面，用redirect比较合适，登录之后还要返回当前页面，因此需要在请求url添加一个回调地址
            response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
            //既然没登录，肯定拦截
            return false;
        }
        //3.取到token之后，调用sso系统的服务校验用户是否登录，既然要用到SSO服务接口，就要依赖taotao-sso-interface
        TaotaoResult taotaoResult = userLoginService.getUserByToken(token);
        //4.如果用户未登录（有token，但已过期，也算是没登录），既没有取到用户信息，跳转sso的等录页面
        //返回的TaotaoResult如果没有登录的话，状态码是400，如果登录了的话，状态码是200
        if (taotaoResult.getStatus() !=200){
            //取得当前的url
            String requestURL=request.getRequestURL().toString();
            //跳转到登录页面，用redirect比较合适，登录之后还要返回当前页面，因此需要在请求url添加一个回调地址
            response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
            //既然没登录，肯定是要拦截的
            return false;
        }
        //5.如果取到用户信息，就放行
        request.setAttribute("USER_INFO",taotaoResult.getData());
        //返回值为true表示放行，返回false表示拦截
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model) throws Exception {
        // handler执行之后，modelAndView返回之前，可以对返回值进行处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        // 在ModelAndView返回之后，这时只能做些异常处理了
    }
}
