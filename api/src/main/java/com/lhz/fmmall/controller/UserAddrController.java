package com.lhz.fmmall.controller;

import com.lhz.fmmall.service.UserAddrService;
import com.lhz.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/useraddr")
@Api(value = "提供收货地址相关接口",tags = "收货地址管理")
public class UserAddrController {

    @Autowired
    private UserAddrService userAddrService;

    @GetMapping("list")
    @ApiImplicitParam(dataType = "int", name = "userId", value = "用户ID", required = true)
    public ResultVO listAddr(Integer userId, @RequestHeader("token") String token) {
        ResultVO resultVO = userAddrService.listAddrsByUid(userId);
        return resultVO;
    }
}
