package com.safvan.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.springframework.stereotype.Component;

@Component
public class PayUtil {
    // appid
    private final String APP_ID = "9021000158626485";
    // 应用私钥
    private final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD2fVHyCdh/qM6J8N+gKv6cnHfBCuVGhwiWpXlUF8oRhvNBejSGaXYUTwkkykJRWN1vappu6iPLYUNlJexJG5j8KPDdhwGYL+HAlD6h9ngtTI+mjkV2g9t/kn2Q3OBGBYJWLkLOL9vA1kmtc9vlqLbIervXBqADFvgnHqDUoyXZCiRvQj8xQd6JApUC/dQpNASLhJo6IltkXJl5pFpLaJliikRhmegcaYQhgTwPV2HOS8kM5AcO8FCSdu//uTHEaHE2SLs/aaRaxOYoVjXiHO+umiOkc9Fiyq6yyIHv4m8MPmyO8wPSnLV+kZi0oPIgGJ1Zb6vvcPKw2oaxvAhe82CBAgMBAAECggEAcNEzLIDtIqD0x5YEBJ8pIi1LcoO2JO2xNKJQQfkRd6w4soNofwzOCsWR/dDFQc5Wq6QzQwjPkUrXOfh9iYikIOXsOLiONkXUIfy4HhFdN/6O0UDU8kWvtVgrOJ6f1+y1lxRdZ1noTv7FgipBP4BVCLWEPArAOinNMrqGCk0SCaAA0SzZnfVIFRgjrvQECX818wqaiROr7iSDVhN2pAziOganM1Z5GyhwfTyHxifA/Gy8/cGDcmAcTSPcM6EkYQwiOBAywy+d1UZSOiJ3Gqod6vpVAQVbBeKKo9D52RkwQC47X3N2SErl1/lrKq+mxBniEg7iiAKZLaaR/qvwRMUzsQKBgQD8i0jOMMA6yDG5W5OtcsZqgs2NOGSSvpofGljQMAfuk8WHrLob3OcR/XaXSvI0WMkenXGWyRDSPhhy272r2l8Cceifqm+4ge1dBa5ucrHfiiEUXsYX36d09dRT+7tpWxlgPQqupwmkOwsGAqSgkp1CEnCGmNyMgFreJyJDNoj5hwKBgQD53NNJGt7I1z4XtdLK3mj2sv12vAwNqKhZk+lry/FbuDIjeo1rWdERhCpH46Kqz9MVIQuJZj1Q0CbVJwJifpm9Yoy8aSg0G/1mGyOe52cHof7qGf8Br6yXh2UPLYMmHHy30Hd1Qw0gVAVEG+Q0b2adx0dXqrhNVb7IR29Yhms3twKBgFEXmIU8+jzl07GlSkJDvJqu7boPBZxpP6wFFcej5KgjIyZxvED+KeSI8u+ZzTQp3krKW5MDdTdNYSRgkwL/18o9MWkznTQt5D4eeOhWQ+93ty+HTyyTUH4qAcgF6SPncLqKX0e9s0bc/qkraBl3eq+gpClUJF2pSYRUJJhYhps5AoGAG8c3HiuElSOI3a3zL2ammLaxgx/I30Pe44aULs3PmXjS6irXyhWELsRYxwFBtmfAedrgo3fgXTmm94+MrlKQ2QM5zkCKnbB27YEa5rGnPKv/4RrexNs4/ueojpVqvTPxkaX1ge3gabWtmDLZnhK8f9IF1nM+ZiqgOTfn4xmeleUCgYEArJlY9cGYZO1ViFDLaxRPqrvKZz+0j8CdX65P/hSqZ5XbfjAUScvbpHHCtH0T28c6IyhWBbUfMJMtxwtrc5ztlxBzpuJrVpO1OwPWeG2unbQDBlX7f6dfOs9BQ0iiZqKkro100eOp2pYJ6s855MmEF/J+APnWl6AlFAS2amWGDlI=";
    private final String CHARSET = "UTF-8";
    // 支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnjhERG/cl3ng/3IlM/4xAS66kdKdCzgLAu/DD006hJzI+I/6YsDpV96KQGWpri/iROM27AGC5/qfy+79kF/jnkcE1/U7F7yO+qm0hykH2FoUk6WLlY8cocUP6rfX9Qc9hdR8GLVB/JjzjnIeWvYgOIdKIZlXOv4XJJmgXwD0bzE0FMnVD0aUTUqWRUrUyJShmeqwijzktKwRk3jpzEZh2mdH003qJfBB/yRGCN7+JM7/2cYjAGXg4jMag7FW5YmYtqm3u3ihZ+BuNZY0qo8iUkfrZCYOjRrT5hxqqQFnboYDiO96NFELs8Yl9KqN3u28p3Pd4HB6MTOuLt+V0O3AsQIDAQAB";
    // 这是沙箱接口路径,正式路径为 https://openapi.alipay.com/gateway.do
    private final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    // 签名方式
    private final String SIGN_TYPE = "RSA2";
    // 支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private final String NOTIFY_URL = "http://x5b99d62.natappfree.cc/api/alipay/toSuccess";
    // 支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private final String RETURN_URL = "http://localhost:9999/TRSApp/user/confirmTrainBooking";

    private AlipayClient alipayClient = null;

    // 支付宝官方提供的接口
    public String sendRequestToAlipay(String outTradeNo, Float totalAmount, String subject) throws AlipayApiException {
        // 获得初始化的AlipayClient
        alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY,
                SIGN_TYPE);

        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(RETURN_URL);
        alipayRequest.setNotifyUrl(NOTIFY_URL);

        // 商品描述（可空）
        String body = "火车票预订";
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\"," +
                "\"total_amount\":\"" + totalAmount + "\"," +
                "\"subject\":\"" + subject + "\"," +
                "\"body\":\"" + body + "\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println("返回的结果是：" + result);
        return result;
    }

    // 通过订单编号查询
    public String query(String id) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", id);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = null;
        String body = null;
        try {
            response = alipayClient.execute(request);
            body = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response != null && response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return body;
    }
}