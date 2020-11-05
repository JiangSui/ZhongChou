package com.offcn.user.component;
import com.offcn.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//发送短信工具类
@Component
@Slf4j //springboot 底层日志处理
public class SmsTemplate {
    @Value("${sms.host}")
    private String host;
    @Value("${sms.path}")
    private String path;
    @Value("${sms.method:POST}")
    private String method;
    @Value("${sms.appcode}")
    private String appcode;

    //发送短信  这里就是用了HttpClient 的服务器请求工具
    public String sendCode(Map<String, String> querys) {
        HttpResponse httpResponse = null; //请求返回的对象
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> bodys = new HashMap<String, String>();
        try {
            if (method.equalsIgnoreCase("get")) {
                httpResponse = HttpUtils.doGet(host, path, method, headers, querys);
            } else {
                httpResponse = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            }
            String string = EntityUtils.toString(httpResponse.getEntity());
            log.info("短信发送完成；响应数据是：{}", string);
            return string;
        } catch (Exception e) {
            log.error("短信发送失败；发送参数是：{}", querys);
            return "fail";
        }
    }
}
