package com.lhz.fmmall.controller;

import com.lhz.fmmall.entity.Users;
import com.lhz.fmmall.service.UserService;
import com.lhz.fmmall.utils.Base64Utils;
import com.lhz.fmmall.vo.ResStatus;
import com.lhz.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(value = "提供用户的登录和注册接口",tags = "用户管理")
@CrossOrigin    //跨域
public class UserController {

    @Resource
    private UserService userService;

    //  url       http://ip:port/fmmall/user/login
    //  params    参数类型    参数个数    参数含义
    //  method    GET
    //  响应数据说明

    // 测试用户：rongrong    密码：rongrong
    @ApiOperation("用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "username",value = "用户登录账号",required = true),
            @ApiImplicitParam(dataType = "String",name = "password",value = "用户登录密码",required = true)
    })
    @GetMapping("/login")
    public ResultVO login(@RequestParam("username") String name,
                          @RequestParam(value = "password") String pwd){
        ResultVO resultVO = userService.checkLogin(name, pwd);
        return resultVO;
    }

    @ApiOperation("用户注册接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String",name = "username",value = "用户注册账号",required = true),
            @ApiImplicitParam(dataType = "String",name = "password",value = "用户注册密码",required = true)
    })
    @PostMapping("/regist")
    public ResultVO regist(@RequestBody Users user){
        //前端传过来的是json格式，需要用@RequestBody将json字符串解析成json对象
        ResultVO resultVO = userService.userResgit(user.getUsername(), user.getPassword());
        return resultVO;
    }

    @ApiOperation("校验token是否过期接口")
    @GetMapping("/check")
    public ResultVO userTokencheck(@RequestHeader("token") String token) {
        return new ResultVO(ResStatus.OK,"success",null);
    }
}
