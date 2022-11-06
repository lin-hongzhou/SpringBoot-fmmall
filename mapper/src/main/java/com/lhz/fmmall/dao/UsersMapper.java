package com.lhz.fmmall.dao;

import com.lhz.fmmall.entity.Users;
import com.lhz.fmmall.general.GeneralDAO;
import org.springframework.stereotype.Repository;

@Repository     //主动注入spring容器，使用@Autowired 注解的不会爆红
public interface UsersMapper extends GeneralDAO<Users> {
}