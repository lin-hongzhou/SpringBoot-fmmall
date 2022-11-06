package com.lhz.fmmall.controller;

import com.github.wxpay.sdk.WXPay;
import com.lhz.fmmall.config.MyPayConfig;
import com.lhz.fmmall.entity.Orders;
import com.lhz.fmmall.service.OrderService;
import com.lhz.fmmall.vo.ResStatus;
import com.lhz.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/order")
@Api(value = "提供订单相关的操作接口", tags = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResultVO add(String cids, @RequestBody Orders order) {
        ResultVO resultVO = null;
        try {
//            Map<String, String> orderInfo = orderService.addOrder(cids, order);
//            String orderId = orderInfo.get("orderId");
            String orderId = "1231231234567";

            if (orderId != null) {
                //设置当前订单信息
                HashMap<String, String> data = new HashMap<>();
//                data.put("body", orderInfo.get("productNames"));
                data.put("body", "咪咪虾条");          //商品描述
                data.put("out_trade_no", orderId);   //使用当前用户订单编号作为当前支付交易的交易号
                data.put("fee_type", "CNY");        //设置支付的币种（CNY：人民币）
//                data.put("total_fee", order.getActualAmount()*100+"");
                data.put("total_fee", "1");      //设置支付金额
                data.put("trade_type", "NATIVE");    //交易类型(NATIVE:二维码)
                data.put("notify_url", " http://r2jxif.natappfree.cc/pay/callback");  //设置支付完成时的回调方法接口
                                                                                      // http://vw7d84.natappfree.cc 是内网穿透的网址
                                                                                      //(该穿透使用netapp，IP地址是动态的)
                //发送请求，获取响应
                //微信支付：申请支付宝连接
                WXPay wxPay = new WXPay(new MyPayConfig());
                Map<String, String> resp = wxPay.unifiedOrder(data);

//                orderInfo.put("payUrl",resp.get("code_url"));
//                resultVO = new ResultVO(ResStatus.NO,"提交订单失败！",orderInfo);
                resultVO = new ResultVO(ResStatus.NO,"提交订单失败！",null);
                System.out.println(resp);
            } else {
                resultVO = new ResultVO(ResStatus.NO,"提交订单失败！",null);
            }
        } catch (SQLException e) {
            resultVO = new ResultVO(ResStatus.NO, "提交订单失败！", null);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    @GetMapping("/status/{oid}")
    public ResultVO getOrderStatus(String orderId,@RequestHeader("token") String token) {
        ResultVO resultVO = orderService.getOrderById(orderId);
        return resultVO;
    }

    @GetMapping("/list")
    @ApiOperation("订单查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(dataType = "String", name = "status", value = "订单状态", required = true),
            @ApiImplicitParam(dataType = "int", name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(dataType = "int", name = "limit", value = "每页条数", required = true)
    })
    public ResultVO list(@RequestHeader("token") String token,
                         String userId, String status, int pageNum, int limit) {
        ResultVO resultVO = orderService.listOrders(userId, status, pageNum, limit);
        return resultVO;
    }
}
