package com.wanglu;

import com.wanglu.util.HttpClientUtil;
import com.wanglu.util.WxPaySignCreateUtil;
import com.wanglu.util.XmlParseUtil;

import java.util.*;

/**
 * Created by wangl on 2017/9/30 0030.
 */
public class APPWxPayTest {

    /**
     * appid(应用)
     * 微信开发平台获取
     * 地址：https://open.weixin.qq.com
     */
    private static String appid = "*****你的appid*****";

    /**
     * mch_id(商户号)
     * 微信支付服务商平台获取
     * 地址：https://pay.weixin.qq.com/index.php/partner/public/home
     */
    private static String mch_id = "*****你的mch_id*****";

    //随机字符串，不长于32位
    private static String nonce_str = "f7zj9vd8rkjuxd6h9z8sei7cvwwru3gb";

    //随便写(中文会出现编码问题)
    private static String body = "wanglu";

    //你的订单号
    private static String out_trade_no = "S2017091317263807458";

    //你的总金额
    private static int total_fee = 100;

    //你的IP
    private static String spbill_create_ip = "172.16.10.174";

    /**
     * notify_url(回调地址)
     * 必须是外网(域名)
     */
    private static String notify_url = "http://www.baidu.com/appWxPay/notify";

    //固定APP
    private static String trade_type = "APP";

    /**
     * 支付秘钥key在微信支付服务商平台手动设置(账户中心-账号设置-API安全) https://pay.weixin.qq.com/index.php/core/cert/api_cert
     * 生成规则：我用的32位 https://suijimimashengcheng.51240.com/
     */
    private static String key = "******你的key*******";

    public static void main(String[] args) throws Exception{

        //生成签名
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("appid", appid);
        parameters.put("mch_id", mch_id);
        parameters.put("body", body);
        parameters.put("nonce_str", nonce_str);
        parameters.put("out_trade_no", out_trade_no);
        parameters.put("total_fee", total_fee);
        parameters.put("spbill_create_ip", spbill_create_ip);
        parameters.put("notify_url", notify_url);
        parameters.put("trade_type", trade_type);
        String characterEncoding = "UTF-8";
        String sign = WxPaySignCreateUtil.createSign(characterEncoding, parameters, key);

        //组装XML
        StringBuffer xmlStr = new StringBuffer();
        xmlStr.append("<xml>");
        xmlStr.append("<appid>"+appid+"</appid>");
        xmlStr.append("<mch_id>"+mch_id+"</mch_id>");
        xmlStr.append("<nonce_str>"+nonce_str+"</nonce_str>");
        xmlStr.append("<sign>"+sign+"</sign>");
        xmlStr.append("<body>"+body+"</body>");
        xmlStr.append("<out_trade_no>"+out_trade_no+"</out_trade_no>");
        xmlStr.append("<total_fee>"+total_fee+"</total_fee>");
        xmlStr.append("<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>");
        xmlStr.append("<notify_url>"+notify_url+"</notify_url>");
        xmlStr.append("<trade_type>"+trade_type+"</trade_type>");
        xmlStr.append("</xml>");

        //HttpClient发送Post请求
        String result = HttpClientUtil.appWxPayHttpClient(xmlStr.toString());

        //返回结果为Xml格式，解析为Map
        Map<String, String> map = XmlParseUtil.doXMLParse(result);

        //获取返回结果中的参数
        String appid = map.get("appid");
        String prepay_id = map.get("prepay_id");
        String nonce_str = map.get("nonce_str");
        String mch_id = map.get("mch_id");
        //当前时间戳
        long timestamp = new Date().getTime()/1000;

        //生成二次签名
        SortedMap<Object,Object> params = new TreeMap<Object, Object>();
        params.put("appid", appid);
        params.put("partnerid", mch_id);
        params.put("prepayid", prepay_id);
        params.put("package", "Sign=WXPay");
        params.put("noncestr", nonce_str);
        params.put("timestamp", timestamp);
        String newSign = WxPaySignCreateUtil.createSign("UTF-8", params, key);

        //生成返回结果的Map
        Map<String, Object> resMap = new HashMap<String, Object>();
        //二次签名
        resMap.put("sign", newSign);
        // 随机码
        resMap.put("nonceStr",nonce_str);
        //商户号
        resMap.put("mch_id", mch_id);
        // 时间
        resMap.put("timestamp", timestamp);

        //返回给APP,APP进行相应操作
        System.out.println(resMap);
    }
}
