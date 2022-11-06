package com.lhz.fmmall.controller;

import com.lhz.fmmall.entity.ShoppingCart;
import com.lhz.fmmall.service.ShoppingCartService;
import com.lhz.fmmall.utils.Base64Utils;
import com.lhz.fmmall.vo.ResStatus;
import com.lhz.fmmall.vo.ResultVO;
import io.jsonwebtoken.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/shopcar")
@Api(value = "提供购物车业务相关接口",tags = "购物车管理")
public class ShopcarController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public ResultVO addShoppingCart(@RequestBody ShoppingCart cart,@RequestHeader("token")String token){
        ResultVO resultVO = shoppingCartService.addShoppingCart(cart);
        return resultVO;
    }

    @GetMapping("/list")
    @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    public ResultVO list(Integer userId,@RequestHeader("token")String token){
        ResultVO resultVO = shoppingCartService.listShoppingCartByUserId(userId);
        return resultVO;
    }

    @PutMapping("/update/{cid}/{cnum}")
    public ResultVO updateNum(@PathVariable("cid") Integer cartId,
                              @PathVariable("cnum") Integer cartNum,
                              @RequestHeader("token") String token) {
        ResultVO resultVO = shoppingCartService.updateCartNum(cartId, cartNum);
        return resultVO;
    }

    @GetMapping("/listbycids")
    @ApiImplicitParam(dataType = "String", name = "cids", value = "选择的购物车记录ID", required = true)
    public ResultVO listByCids(String cids, @RequestHeader("token")String token){
        ResultVO resultVO = shoppingCartService.listShoppingCartsByCids(cids);
        return resultVO;
    }

}
