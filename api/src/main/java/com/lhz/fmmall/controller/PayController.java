package com.lhz.fmmall.controller;

import com.github.wxpay.sdk.WXPayUtil;
import com.lhz.fmmall.interceptor.websocket.WebSocketServer;
import com.lhz.fmmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    /**
     * 回调接口：当用户支付成功之后，微信支付平台就会请求这个接口，将支付状态的数据传递过来
     * 2、接收微信支付平台传递的数据（使用request 的输入流接收）
     */

    @RequestMapping("/callback")
    public String success(HttpServletRequest request) throws Exception{
        System.out.println("--------------callback");
        //1、接收微信支付平台传递的数据（使用request的输入流接收）
        ServletInputStream is = request.getInputStream();
        byte[] bs = new byte[1024];
        int len = -1;
        StringBuilder builder = new StringBuilder();
        while ((len = is.read(bs)) != -1) {
            builder.append(new String(bs,0,len));
        }
        String s = builder.toString();
        //使用帮助类将xml接口的字符串转换成 map
        Map<String, String> map = WXPayUtil.xmlToMap(s);

        if (map != null && "success".equalsIgnoreCase(map.get("result_code"))) {
            //支付成功
            //2、修改订单状态为“代发货/已支付”
            String orderId = map.get("out_trade_no");
            int i = orderService.updateOrderStatus(orderId, "2");
            System.out.println("-----------------" + orderId);
            //3、通过websocket连接，向前端推送消息
            WebSocketServer.sendMsg(orderId,"1");
            //4、响应微信支付平台
            if (i > 0) {
                HashMap<String, String> resp = new HashMap<>();
                resp.put("return_code", "success");
                resp.put("return_msg", "OPK");
                resp.put("appid", map.get("appid"));
                resp.put("result_code", "success");
                return WXPayUtil.mapToXml(resp);
            }
        }
        return null;
    }
}
