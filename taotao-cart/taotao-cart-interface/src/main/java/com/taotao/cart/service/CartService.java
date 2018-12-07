package com.taotao.cart.service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

import java.util.List;

/**
 * Created by Peng on 2018/11/28.
 */
public interface CartService {
    /**
     * 添加购物车
     */
    public TaotaoResult addItemCart(TbItem tbItem,Integer num,Long userId);

    /*
    * 根据用户ID查询用户的购物车列表
    * */
    public List<TbItem> getCartList(Long userId);


    /*
    * 根据商品ID 更新数量
    * @param itemId 商品的Id
    * @param num 商品的数量
    * @param userId 用户的ID 购物车的ID
    * @Return
    * */
    public TaotaoResult updateItemCartByItemId(Long userId,Long itemId,Integer num);

    public TaotaoResult deleteItemCartByItemId(Long userId,Long itemId);


}
