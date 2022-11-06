package com.lhz.fmmall.dao;

import com.lhz.fmmall.entity.ShoppingCart;
import com.lhz.fmmall.entity.ShoppingCartVO;
import com.lhz.fmmall.general.GeneralDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartMapper extends GeneralDAO<ShoppingCart> {

    //根据用户ID查询购物车
    public List<ShoppingCartVO> selectShopcartByUserId(int userId);

    //根据购物车ID修改商品数量
    public int updateCartnumByCartid(@Param("cartId") int cartId, @Param("cartNum") int cartNum);

    public List<ShoppingCartVO> selectShopcartByCids(List<Integer> cids);
}