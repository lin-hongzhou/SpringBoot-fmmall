package com.lhz.fmmall.service.impl;

import com.lhz.fmmall.dao.OrderItemMapper;
import com.lhz.fmmall.dao.OrdersMapper;
import com.lhz.fmmall.dao.ProductSkuMapper;
import com.lhz.fmmall.dao.ShoppingCartMapper;
import com.lhz.fmmall.entity.*;
import com.lhz.fmmall.service.OrderService;
import com.lhz.fmmall.utils.PageHelper;
import com.lhz.fmmall.vo.ResStatus;
import com.lhz.fmmall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;

    /**
     * 保存订单业务
     * @param cids  前端传来购物车ID
     * @param order 前端传来订单
     * @return
     */

    @Transactional  //springboot自带的事务管理
    public Map<String, String> addOrder(String cids, Orders order) throws SQLException {
        Map<String, String> map = new HashMap<>();

        //1、校验库存，根据cids查询当前订单中关联的购物车记录详情（包括库存）
        String[] arr = cids.split(",");
        List<Integer> cidsList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            cidsList.add(Integer.parseInt(arr[i]));
        }
        List<ShoppingCartVO> list = shoppingCartMapper.selectShopcartByCids(cidsList);
        boolean f = true;
        String untitled = "";
        for (ShoppingCartVO sc : list) {
            if (Integer.parseInt(sc.getCartNum()) > sc.getSkuStock()) {
                f = false;
            }
            //获取所有商品名称，以 , 分割拼接成字符串
            untitled = untitled + sc.getProductName() + ",";
        }

        if (f) {
            //3、表示库存充足 ----保存订单
            //a.userId
            //b.untitled
            //c.收货人信息：姓名、电话、地址
            //d.总价格
            //e.之父方式
            //f.订单创建时间
            //g.订单初始状态(待支付  1)
            order.setUntitled(untitled);
            order.setCreateTime(new Date());
            order.setStatus("1");

            //生成订单编号
            String orderId = UUID.randomUUID().toString().replace("-", "");
            order.setOrderId(orderId);

            //2、保存订单
            int i = ordersMapper.insert(order);
            if (i > 0) {
                //3、生成商品快照
                for (ShoppingCartVO sc : list) {
                    int cnum = Integer.parseInt(sc.getCartNum());
                    String itemId = System.currentTimeMillis() + "" + (new Random().nextInt(89999) + 10000);    //生成随机订单ID
                    OrderItem orderItem = new OrderItem(itemId, orderId, sc.getProductId(), sc.getProductName(),
                            sc.getProductImg(), sc.getSkuId(), sc.getSkuName(), new BigDecimal(sc.getSellPrice()),
                            cnum, new BigDecimal(sc.getSellPrice() * cnum), new Date(), new Date(), 0);
                    int m = orderItemMapper.insert(orderItem);
                }

                //4、扣减库存：根据套餐ID修改套餐库存量
                //  商品1     奥利奥小饼干      套餐ID 4          2           500
                //  商品2     咪咪虾条         套餐ID 1          2           127

                for (ShoppingCartVO sc : list) {
                    String skuId = sc.getSkuId();
                    int newStock = sc.getSkuStock() - Integer.parseInt(sc.getCartNum());

                    //第一种，需要操作两次数据库
                    //ProductSku productSku = productSkuMapper.selectByPrimaryKey(skuId);
                    //productSku.setStock(newStock);
                    //int k = productSkuMapper.updateByPrimaryKey(productSku);

                    //第二种，只需要操作一次数据库
                    ProductSku productSku = new ProductSku();
                    productSku.setSkuId(skuId);
                    productSku.setStock(newStock);
                    int k = productSkuMapper.updateByPrimaryKeySelective(productSku);
                }
            }

            //5、删除购物车：当购物车中的记录购买成功之后，购物车种对应做删除操作
            for (int cid : cidsList) {
                shoppingCartMapper.deleteByPrimaryKey(cid);
            }

            map.put("orderId",orderId);
            map.put("productNames",untitled);
            return map;
        } else {
            //表示库存不足
            return null;
        }
    }

    @Override
    public int updateOrderStatus(String orderId, String status) {
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setStatus(status);
        int i = ordersMapper.updateByPrimaryKeySelective(orders);
        return i;
    }

    @Override
    public ResultVO getOrderById(String orderId) {
        Orders order = ordersMapper.selectByPrimaryKey(orderId);
        return new ResultVO(ResStatus.OK,"success",order.getStatus());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)  //事务锁
    public void closeOrder(String orderId) {
        synchronized (this) {   //JVM锁
            //  1.修改当前订单：status=6 已关闭  close_type=1 超时未支付
            Orders cancleOrder = new Orders();
            cancleOrder.setOrderId(orderId);
            cancleOrder.setStatus("6"); //已关闭
            cancleOrder.setCloseType(1);    //关闭类型：超时未支付
            ordersMapper.updateByPrimaryKeySelective(cancleOrder);

            //  2.还原库存：先根据当前订单编号查询商品快照（skuid  buy_count）    --->   修改product_sku
            Example example1 = new Example(OrderItem.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orderId", orderId);
            List<OrderItem> orderItems = orderItemMapper.selectByExample(example1);
            //还原库存
            for (int j = 0; j < orderItems.size(); j++) {
                OrderItem orderItem = orderItems.get(j);
                //修改
                ProductSku productSku = productSkuMapper.selectByPrimaryKey(orderItem.getSkuId());
                productSku.setStock(productSku.getStock() + orderItem.getBuyCounts());
                productSkuMapper.updateByPrimaryKeySelective(productSku);
            }
        }
    }

    @Override
    public ResultVO listOrders(String userId, String status, int pageNum, int limit) {
        //1、分页查询
        int start = (pageNum-1)*limit;
        List<OrdersVO> ordersVOS = ordersMapper.selectOrders(userId, status, pageNum, limit);

        //2、查询总记录数
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("userId",userId);
        if (status !=null && !"".equals(status)){
            criteria.andLike("status",status);
        }
        int count = ordersMapper.selectCountByExample(example);

        //3、计算总页数
        int pageCount = count%limit==0?count/limit:count/limit+1;

        //4、封装数据
        PageHelper<OrdersVO> pageHelper = new PageHelper<>(count, pageCount, ordersVOS);
        return new ResultVO(ResStatus.OK,"SUCCESS",pageHelper);
    }
}
