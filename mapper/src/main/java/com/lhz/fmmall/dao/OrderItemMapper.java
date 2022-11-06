package com.lhz.fmmall.dao;

import com.lhz.fmmall.entity.OrderItem;
import com.lhz.fmmall.general.GeneralDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemMapper extends GeneralDAO<OrderItem> {

    public List<OrderItem> listOrderItemsByOrderId(String orderId);
}