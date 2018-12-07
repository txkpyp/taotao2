package com.taotao.cart.controller;

import com.alibaba.fastjson.JSON;
import com.taotao.cart.service.CartService;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import com.taotao.sso.service.UserLoginService;
import com.taotao.util.CookieUtils;
import com.taotao.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peng on 2018/11/25.
 */
@Controller
public class CartController {


    @Autowired
    private CartService cartService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private ItemService itemService;

    @Value("${CART_KEY}")
    private String CART_KEY;

    @Value("${CART_EXPIRE}")
    private Integer CART_EXPIRE;

    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;


    /**
     * 添加商品到购物车中，并保存到cookie中
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addItemCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                              HttpServletRequest request, HttpServletResponse response){

        //引入服务
        //注入服务
        //调用服务

        //判断用户有没有登录
        //从cookie中获取用户的token信息
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        //调用SSO的服务查询用户的信息
        TaotaoResult result = userLoginService.getUserByToken(token);
        //获取商品的数据
        TbItem item = itemService.getItemById(itemId);
        //判断是否已登录
        if (result.getStatus()==200){
            //如果已登录，调用service的方法
            TbUser user= (TbUser) result.getData();
            cartService.addItemCart(item, num, user.getId());
        }else{
            //如果没有登录，调用设置到cookie中方法
            //现根据cookie获取购物车列表
            //1.取出cookie中的购物车列表
            List<TbItem> itemList = getCartItemList(request);
            //2.判断商品是否存在
            boolean flag=false;
            for (TbItem item2:itemList){
                //由于tbitem中的id与参数中的itemID都是包装类型的long，要比较是否相等不要用==，因为那样比较
                //的是对象的地址而不是值，为了让它们比较的是值，那么可以使用.longValue来获取值
                if (item2.getId() == itemId.longValue()){
                    //如果存在，数量相加
                    item2.setNum(item2.getNum()+num);
                    flag=true;
                    break;
                }
            }
            //如果不存在，则新增一个商品
            if (!flag){
                //1.引入服务
                //2.注入服务
                //3.调用服务
                TbItem tbItem = itemService.getItemById(itemId);
                //设置商品的数量
                tbItem.setNum(num);
                //取一张图片
                String image=tbItem.getImage();
                if (!StringUtils.isBlank(image)){
                    tbItem.setImage(image.split(",")[0]);
                }
                //把商品添加到购物车
                itemList.add(tbItem);
            }
            //把购物车列表写到cookie中,并设置有效期
            CookieUtils.setCookie(request,response,CART_KEY,JSON.toJSONString(itemList),CART_EXPIRE,true);

        }

        //返回成功页面
        return "cartSuccess";
    }

    /**
     * 展示购物车列表数据
     * @param request
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request){
        //引入服务
        //注入服务
        //判断用户是否登录
        //从cookie中获取用户的token信息
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        //调用SSO的服务查询用户的信息
        TaotaoResult result = userLoginService.getUserByToken(token);

        //获取商品数据
        if (result.getStatus() == 200){
            //如果已登录。调用service方法
            TbUser user= (TbUser) result.getData();
            List<TbItem> cartList = cartService.getCartList(user.getId());
            request.setAttribute("cartList",cartList);
        }else {
            //如果没有登录，调用cookie的方法
            //从cookie中获取购物车列表数据
            List<TbItem> tbItemList = getCartItemList(request);
            //把购物车的列表传递给jsp
            request.setAttribute("cartList",tbItemList);
        }


        //返回逻辑视图
        return "cart";
    }

    /**
     * 修改购物车商品的数量
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateItemNum(@PathVariable Long itemId,@PathVariable Integer num,
                                      HttpServletRequest request,HttpServletResponse response){

        //引入服务
        //注入服务
        //调用服务
        //判断用户是否已登录
        //从cookie中获取用户的token信息
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        //获取用户信息
        TaotaoResult result = userLoginService.getUserByToken(token);

        if (result.getStatus() == 200){
            //如果已登录。调用service方法，获取商品数据
            TbUser user= (TbUser) result.getData();
            //更新数据
            cartService.updateItemCartByItemId(user.getId(),itemId,num);
        }else {
            //如果没有登录，则调用cookie中的方法，更新cookie中的数据
            //从cookie中获取购物车商品列表
            List<TbItem> itemList = getCartItemList(request);
            //查询得到对应的商品
            boolean flag=false;
            for (TbItem item:itemList){
                if (item.getId() == itemId.longValue()){
                    //更新商品数量
                    item.setNum(num);
                    flag=true;
                    break;
                }
            }
            if (flag){
                //如果存在
                //把购物车列表写入cookie中
                CookieUtils.setCookie(request,response,CART_KEY,JSON.toJSONString(itemList),CART_EXPIRE,true);
            }
        }
        return TaotaoResult.ok();
    }

    /**
     *删除购物车商品，并更新cookie的值
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteItemById(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
        //1.引入服务
        //2.注入服务
        //3.调用服务
        //判断用是否已登录
        //根据cookie中的key来获取用户的token信息
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        //根据token获取用户的信息
        TaotaoResult result = userLoginService.getUserByToken(token);

        if (result.getStatus() == 200){
            //如果已登录，则调用service方法，删除商品
            TbUser user= (TbUser) result.getData();
            //删除数据
            cartService.deleteItemCartByItemId(user.getId(),itemId);
        }else
        {
            //如果没有登录，这调用cookie中的方法，删除商品
            //从cookie中获取购物车列表
            boolean flag=false;
            List<TbItem> tbItemList = getCartItemList(request);
            for (TbItem tbItem:tbItemList){
                if (tbItem.getId() == itemId.longValue()){
                    tbItemList.remove(tbItem);
                    flag=true;
                    break;
                }
            }
            if (flag){
                //把购物车商品写入cookie中
                CookieUtils.setCookie(request,response,CART_KEY, JSON.toJSONString(tbItemList),CART_EXPIRE,true);
            }
        }
        //重定向到购物车页面
        return "redirect:/cart/cart.html";
    }

    //从cookie中取得商品数据
    public List<TbItem> getCartItemList(HttpServletRequest request){
        //从cookie中获取购物车商品列表的数据
        String json = CookieUtils.getCookieValue(request, CART_KEY,true);//为了防止乱码，统一下编码格式
        if (StringUtils.isBlank(json)){//判断cookie中时候为空
            //为空说明cookie中没有商品。返回一个空的List<Tbitem>
            return new ArrayList<TbItem>();
        }
        List<TbItem> tbItemList = JSON.parseArray(json, TbItem.class);
        return tbItemList;
    }

}
