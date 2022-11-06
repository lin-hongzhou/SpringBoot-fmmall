package com.lhz.fmmall.service;

import com.lhz.fmmall.vo.ResultVO;

public interface UserAddrService {

    //根据用户ID查询地址列表
    public ResultVO listAddrsByUid(int userId);
}
