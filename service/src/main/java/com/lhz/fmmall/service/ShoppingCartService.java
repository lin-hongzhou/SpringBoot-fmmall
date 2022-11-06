package com.lhz.fmmall.service;

import com.lhz.fmmall.entity.ShoppingCart;
import com.lhz.fmmall.vo.ResultVO;

import java.util.List;

public interface ShoppingCartService {

    //添加商品到购物车
    public ResultVO addShoppingCart(ShoppingCart cart);

    //根据用户ID查询购物车列表
    public ResultVO listShoppingCartByUserId(int userId);

    //根据购物车ID修改商品数量
    public ResultVO updateCartNum(int cartId, int cartNum);

    //查询购物车列表
    public ResultVO listShoppingCartsByCids(String cids);
}
