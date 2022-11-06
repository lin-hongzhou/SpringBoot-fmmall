package com.lhz.fmmall.service;

import com.lhz.fmmall.entity.Orders;
import com.lhz.fmmall.vo.ResultVO;

import java.sql.SQLException;
import java.util.Map;

public interface OrderService {

    //根据购物车ID以及订单详情添加订单
    public Map<String,String> addOrder(String cids, Orders order) throws SQLException;

    //修改订单状态
    public int updateOrderStatus(String orderId, String status);

    //查询订单状态
    public ResultVO getOrderById(String orderId);

    public void closeOrder(String orderId);

    public ResultVO listOrders(String userId, String status, int pageNum, int limit);
}
