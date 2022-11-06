package com.lhz.fmmall.service.impl;

import com.lhz.fmmall.dao.ShoppingCartMapper;
import com.lhz.fmmall.entity.ShoppingCart;
import com.lhz.fmmall.entity.ShoppingCartVO;
import com.lhz.fmmall.service.ShoppingCartService;
import com.lhz.fmmall.vo.ResStatus;
import com.lhz.fmmall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    //查询购物车信息
    @Override
    public ResultVO addShoppingCart(ShoppingCart cart) {
        cart.setCartTime(sdf.format(new Date()));
        int i = shoppingCartMapper.insert(cart);
        if (i > 0) {
            return new ResultVO(ResStatus.OK,"success",null);
        }else {
            return new ResultVO(ResStatus.NO,"fail",null);
        }
    }

    //购物车列表查询
    @Transactional(propagation = Propagation.SUPPORTS)
    public ResultVO listShoppingCartByUserId(int userId) {
        List<ShoppingCartVO> list = shoppingCartMapper.selectShopcartByUserId(userId);
        ResultVO resultVO = new ResultVO(ResStatus.OK, "success", list);
        return resultVO;
    }

    @Override
    public ResultVO updateCartNum(int cartId, int cartNum) {
        int i = shoppingCartMapper.updateCartnumByCartid(cartId,cartNum);
        if (i > 0) {
            return new ResultVO(ResStatus.OK,"update success",null);
        } else {
            return new ResultVO(ResStatus.NO,"update fail",null);
        }
    }

    @Override
    public ResultVO listShoppingCartsByCids(String cids) {
        //使用 tkMapper 只能查询到某张表中拥有的字段，因此没法查询到商品名称、图片、单价等信息
        //Example example = new Example(ShoppingCartVO.class);
        //Example.Criteria criteria = example.createCriteria();
        //criteria.andEqualTo("cartId",cids);
        //List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByExample(example);

        String[] arr = cids.split(",");     //将字符串转换成字符串数组
        List<Integer> cartIds = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {  //遍历字符串数组，转换成整型
            cartIds.add(Integer.parseInt(arr[i]));
        }
        List<ShoppingCartVO> list = shoppingCartMapper.selectShopcartByCids(cartIds);
        ResultVO resultVO = new ResultVO(ResStatus.OK, "success", list);
        return resultVO;
    }
}
