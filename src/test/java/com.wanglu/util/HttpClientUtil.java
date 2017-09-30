package com.wanglu.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by 王璐 on 2017/8/30 0030.
 * 注意:最新版的httpClient使用实现类的是closeableHTTPClient,以前的default作废了
 */
public class HttpClientUtil {


    public static String appWxPayHttpClient(String xml) throws Exception{

        String content = null;

        //1.使用帮助类HttpClients创建CloseableHttpClient对象.
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2. 基于要发送的HTTP请求类型创建HttpGet或者HttpPost实例.
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");

        //3.设置请求头
        httpPost.addHeader("content-type", "application/x-www-form-urlencoded");

        CloseableHttpResponse httpResponse = null;
        try {
            //4. 通过执行此HttpGet或者HttpPost请求获取CloseableHttpResponse实例
            StringEntity entity = new StringEntity(xml);
            httpPost.setEntity(entity);

            httpResponse = httpClient.execute(httpPost);

            //5. 从此CloseableHttpResponse实例中获取状态码,错误信息,以及响应页面等等.
            if (httpResponse.getStatusLine().getStatusCode() == 200){
                content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //6. 最后关闭HttpClient资源.
            if (null != httpResponse){
                httpResponse.close();
            }
            httpClient.close();
        }
        return content;
    }
}
