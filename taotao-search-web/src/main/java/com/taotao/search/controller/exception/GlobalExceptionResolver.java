package com.taotao.search.controller.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Peng on 2018/11/14.
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver{

    //获取logger
    private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse, Object o, Exception e) {

        logger.info("进去全局异常处理。。。。");
        logger.debug("测试handller的类型："+o.getClass());
        //控制台打印输出异常
        e.printStackTrace();
        //向日志文件写入异常
        logger.error("系统发生异常：",e);
        //发送邮件（使用jmail客户端进行发送）
        //发送短信
        //展示错误页面
        ModelAndView model=new ModelAndView();
        model.addObject("message","当前网络故障，请稍后重试！");
        //返回逻辑视图，这样回去访问error目录下的error.jsp
        model.setViewName("error/exception");
        return model;
    }
}
