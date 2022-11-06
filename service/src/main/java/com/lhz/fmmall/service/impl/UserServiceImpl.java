package com.lhz.fmmall.service.impl;

import com.lhz.fmmall.dao.UsersMapper;
import com.lhz.fmmall.entity.Users;
import com.lhz.fmmall.service.UserService;
import com.lhz.fmmall.utils.MD5Utils;
import com.lhz.fmmall.vo.ResStatus;
import com.lhz.fmmall.vo.ResultVO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Transactional
    public ResultVO userResgit(String name, String pwd) {
        synchronized (this) {
            //1、根据用户查询，这个用户是否已经被注册
            Example example = new Example(Users.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("username",name);
            List<Users> users = usersMapper.selectByExample(example);
            //2、如果没有被注册则进行保存操作
            if (users.size() == 0) {
                String md5pwd = MD5Utils.md5(pwd);
                Users user = new Users();
                user.setUsername(name);
                user.setPassword(md5pwd);
                user.setUserImg("/img/default.png");
                user.setUserRegtime(new Date());
                user.setUserModtime(new Date());
                int i = usersMapper.insert(user);
                if (i > 0) {
                    return new ResultVO(ResStatus.OK, "注册成功！", user);
                } else {
                    return new ResultVO(ResStatus.NO, "注册失败！", null);
                }
            } else {
                return new ResultVO(ResStatus.NO, "用户名已经被注册！", null);
            }
        }
    }

    @Override
    public ResultVO checkLogin(String name, String pwd) {
        //1、根据账号查询用户信息
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",name);
        List<Users> users = usersMapper.selectByExample(example);

        //2、判断
        if (users.size() == 0){
            //用户名不存在
            return new ResultVO(ResStatus.NO,"登录失败，用户名不存在",null);
        }else {
            //3、对输入的密码pwd进行进行加密
            String md5pwd = MD5Utils.md5(pwd);
            //使用加密后的密码 和 user 中的密码进行匹配
            if (md5pwd.equals(users.get(0).getPassword())){ //users.get(0) 表示List集合里的第一个数组
                //如果登录验证成功，则需要生成令牌token（token就是按照特定规则生成的字符串）
                //String token = Base64Utils.encode(name+123456);

                //使用jwt规则生成token字符串
                JwtBuilder builder = Jwts.builder();

                HashMap<String,Object> map = new HashMap<>();
                map.put("key1","value1");
                map.put("key2","value2");

                String token = builder.setSubject(name)                          //主题，就是token中携带的数据
                        .setIssuedAt(new Date())                                 //设置token的生成时间
                        .setId(users.get(0).getUserId() + "")                    //设置用户 id 为 token  id
                        .setClaims(map)                 //map 中可以存放用户的角色权限信息
                        .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))  //设置token过期时间
                        .signWith(SignatureAlgorithm.HS256, "rongrong666")                 //设置加密方式和加密密码
                        .compact();

                return new ResultVO(ResStatus.OK,token,users.get(0));
            }else {
                //密码错误
                return new ResultVO(ResStatus.NO,"登录失败，密码错误",null);
            }
        }
    }
}
