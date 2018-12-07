package com.taotao.order.controller;

import com.alibaba.fastjson.JSON;
import com.taotao.cart.service.CartService;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peng on 2018/11/25.
 */
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Value("${CART_KEY}")
    private String CART_KEY;


    /**
     * url:/order/order-cart.html
     * 参数：没有参数，但需要用户的id  从cookie中获取token 调用SSO的服务获取用户的ID
     * 返回值：逻辑视图 （订单的确认页面）
     */
    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request, HttpServletResponse response){

//1.从cookie中获取用户的token
//		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
//		if(StringUtils.isNotBlank(token)){
//			//2.调用SSO的服务获取用户的信息
//			TaotaoResult result = loginservice.getUserByToken(token);
//			if(result.getStatus()==200){
//				//3.必须是用户登录了才展示
//				//4.展示用户的送货地址   根据用户的ID查询该用户的配送地址。静态数据
//				//5.展示支付方式    从数据库中获取支付的方式。静态数据
//				//6.调用购物车服务从redis数据库中获取购物车的商品的列表
//				TbUser user = (TbUser)result.getData();
//				List<TbItem> cartList = cartservice.getCartList(user.getId());
//				//7.将列表 展示到页面中(传递到页面中通过model)
//				request.setAttribute("cartList", cartList);
//			}
//		}
        //获取用户的登录信息
        TbUser user= (TbUser) request.getAttribute("USER_INFO");
        //从redis中获取商品列表数据
        List<TbItem> cartList = cartService.getCartList(user.getId());
        //从cookie中获取购物车商品列表到页面
        List<TbItem> cartList2 = getCartItemList(request);

        //合并数据到redis中
        for (TbItem tbItem : cartList2) {
            boolean flag=false;
            if (tbItem!=null){
                for (TbItem item : cartList) {//数据库中的数据
                    //意味着数据库中的购物车有数据，就应该数量添加
                    if (tbItem.getId()==item.getId().longValue()){
                        //添加数量，并调用service的方法
                        item.setNum(item.getNum()+tbItem.getNum());
                        cartService.updateItemCartByItemId(user.getId(),item.getId(),item.getNum());
                        flag=true;//表示数据找到
                    }
                }
            }
            if (!flag){//如果数据找了一轮没找到，说明cookie中的数据是新的，需要添加
                cartService.addItemCart(tbItem,tbItem.getNum(),user.getId());
            }
        }
        //合并数据后，删除cookie中的数据
        if (!cartList2.isEmpty()){
            CookieUtils.deleteCookie(request,response,CART_KEY);
        }
        //重新从数据库获取数据
        cartList = cartService.getCartList(user.getId());
        request.setAttribute("cartList",cartList);
        //返回逻辑视图
        return "order-cart";
    }


    /**
     * url:/order/create
     * 参数：表单使用orderinfo来接收
     *返回值：逻辑视图
     * @param orderInfo
     * @return
     */
    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo,HttpServletRequest request,Model model){
        //1，引入服务
        //2.注入服务
        //3.调用服务
        //查询用户的信息，设置到info中
        TbUser user= (TbUser) request.getAttribute("USER_INFO");
        //设置用户属性
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());

        //生成订单
        TaotaoResult result = orderService.createOrder(orderInfo);
        model.addAttribute("orderId",result.getData());
        model.addAttribute("payment",orderInfo.getPayment());
        //获取三天后的日期
        DateTime dateTime=new DateTime();
        DateTime plusDays = dateTime.plusDays(3);//加3天
        model.addAttribute("date",plusDays.toString("yyyy-MM-dd"));
        return "success";
    }


    private List<TbItem> getCartItemList(HttpServletRequest request){
        //从cookie中获取购物车商品列表
        String json = CookieUtils.getCookieValue(request, CART_KEY,true);//为了防止乱码，统一下编码格式
        if (StringUtils.isBlank(json)){
            //说明cookie中没有商品列表，那么返回一个空的列表
            return new ArrayList<TbItem>();
        }
        List<TbItem> list= JSON.parseArray(json,TbItem.class);
        return list;
    }
}
