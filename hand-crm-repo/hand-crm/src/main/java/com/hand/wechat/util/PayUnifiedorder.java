package com.hand.wechat.util;

import com.alibaba.fastjson.JSONObject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 统一下单签名
 */
public class PayUnifiedorder {

    public static final PayUnifiedorder api = new PayUnifiedorder();
    private PayUnifiedorder(){}


    public JSONObject unifiedorder(Environment env, HttpServletRequest request, String trade_type,
                                   String appID, String partner, String partnerKey,
                                   String orderId, String productName, String total,
                                   String outTradeNo, String openid, String scene_info,
                                   String ip) {
        JSONObject jsonObject = new JSONObject();
        String total_fee = total;
        String body = productName;
        String out_trade_no = outTradeNo;
        String attach_ = "{\"orderId\":\"" + orderId + "\"}";
        String notifyUrl = env.getProperty("domainName") + env.getProperty("pay.notifyUrl");

        String appid = env.getProperty(appID);
//        String appsecret = env.getProperty(appSecret);
        String mch_id = env.getProperty(partner);
        String partnerkey = env.getProperty(partnerKey);
        // //设备号 非必输
        // String device_info="";
        // //获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
        // String currTime = TenpayUtil.getCurrTime();
        // //8位日期
        // String strTime = currTime.substring(8, currTime.length());
        // //四位随机数
        // String strRandom = TenpayUtil.buildRandom(4) + "";
        // //10位序列号,可以自行调整。
        // String strReq = strTime + strRandom;
        // 随机字符串
        String nonce_str = UUID.randomUUID().toString().replace("-", "");
        // 商品描述根据情况修改
        // String body = "";
        // //商品详情
        // String detail = "";
        // //附加数据(代金券ID)
        // 可作为自定义参数
        String attach = attach_;
        // //商户订单号
        // String out_trade_no = appid+BnRandom.getName();
        // //货币类型
        // String fee_type = "CNY";
        // 总金额
        // String total_fee = "1";
        // 订单生成的机器 IP
//        String spbill_create_ip = request.getRemoteAddr();
        String spbill_create_ip = IpUtil.api.getIpAddress(request);
        if(!StringUtils.isEmpty(ip)) {
            spbill_create_ip = ip;
        }
        // //订 单 生 成 时 间 非必输
        // String time_start ="";
        // //订单失效时间 非必输
        // SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        // String time_expire = format.format((new Date().getTime()-500000));
        // //商品标记 非必输
        // String goods_tag = "";
        // 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
        String notify_url = notifyUrl;
        // 交易类型
//        String trade_type = "APP";//"JSAPI";
        // //商品ID
        // String product_id = "";
        // //指定支付方式
        // String limit_pay = "";
        // 用户标识
        OutputStream outputStream = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        final StringBuffer stringBuffer = new StringBuffer();
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("attach", attach);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", total_fee);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);
        if(!StringUtils.isEmpty(openid)) {
            packageParams.put("openid", openid);
        }
        if(!StringUtils.isEmpty(scene_info)) {
            packageParams.put("scene_info", scene_info);
        }
        String sign = createSign(packageParams, partnerkey);

        try {
            String UTF8 = "UTF-8";
            String reqBody =
                    "<xml>" +
                            "<appid>" + appid + "</appid>" +
                            "<attach>" + attach + "</attach>" +
                            "<mch_id>" + mch_id + "</mch_id>" +
                            "<nonce_str>" + nonce_str + "</nonce_str>" +
                            "<sign>" + sign + "</sign>" +
                            "<body>" + body + "</body>" +
                            "<out_trade_no>" + out_trade_no + "</out_trade_no>" +
                            "<total_fee>" + total_fee + "</total_fee>" +
                            "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" +
                            "<notify_url>" + notify_url + "</notify_url>" +
                            "<trade_type>" + trade_type + "</trade_type>";
            if(!StringUtils.isEmpty(openid)) {
                reqBody += "<openid>" + openid + "</openid>";
            }
            if(!StringUtils.isEmpty(scene_info)) {
                reqBody += "<scene_info>" + scene_info + "</scene_info>";
            }
            //当trade_type=JSAPI时 传入openId
            if(!StringUtils.isEmpty(trade_type) && trade_type.equals("JSAPI")){
                reqBody += "<openid>" + openid + "</openid>";
            }
            reqBody += "</xml>";
            URL httpUrl = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");
            HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
            httpURLConnection.setRequestProperty("Host", "api.mch.weixin.qq.com");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10 * 1000);
            httpURLConnection.setReadTimeout(10 * 1000);
            httpURLConnection.connect();
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(reqBody.getBytes(UTF8));
            System.out.println(" PayUnifiedorder reqBody:");
            System.out.println(reqBody);
            //获取内容
            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String resp = stringBuffer.toString();
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(" PayUnifiedorder resp "+resp);
        String prepay_id = "";
        String code_url = "";
        String mweb_url = "";
        try {
            Map<String, String> xmlMap = doXMLParse(resp);
            String return_code = xmlMap.get("return_code");
            String result_code = xmlMap.get("result_code");

            if (!StringUtils.isEmpty(return_code) && return_code.equals("SUCCESS") &&
                    !StringUtils.isEmpty(result_code) && result_code.equals("SUCCESS")) {
                prepay_id = xmlMap.get("prepay_id");
                if(xmlMap.containsKey("code_url")) {
                    code_url = xmlMap.get("code_url");
                }
                if(xmlMap.containsKey("mweb_url")) {
                    mweb_url = xmlMap.get("mweb_url");
                }
            } else {
                String err_code = xmlMap.get("err_code");
                if(!StringUtils.isEmpty(err_code) && err_code.equals("ORDERPAID")){
                    jsonObject.put("errcode", "2");
                    jsonObject.put("errmsg", "该订单已支付");
                }else {
                    jsonObject.put("errcode", "1");
                    jsonObject.put("errmsg", "统一下单签名失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String nonceStr2 = nonce_str;
        String timestamp = (System.currentTimeMillis() / 1000) + "";
        String prepay_id2 = "prepay_id=" + prepay_id;
        String packages = prepay_id2;
        String appid2 = appid;

        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        String finalSign = "";
        if(!trade_type.equals("APP")) {
            finalpackage.put("appId", appid2);
            finalpackage.put("timeStamp", timestamp);
            finalpackage.put("nonceStr", nonceStr2);
            finalpackage.put("package", packages);
            finalpackage.put("signType", "MD5");
        } else {
            finalpackage.put("appid", appid2);
            finalpackage.put("noncestr", nonceStr2);
            finalpackage.put("package", "Sign=WXPay");
            finalpackage.put("partnerid", mch_id);
            finalpackage.put("prepayid", prepay_id);
            finalpackage.put("sign", sign);
            finalpackage.put("timestamp", timestamp);
        }
        finalSign = createSign(finalpackage, partnerkey);

        if (!jsonObject.containsKey("errcode")) {
            jsonObject.put("errcode", "0");
            jsonObject.put("errmsg", "统一下单签名成功");
            jsonObject.put("partnerId", mch_id);
            jsonObject.put("appid", appid);
            jsonObject.put("timeStamp", timestamp);
            jsonObject.put("nonceStr", nonceStr2);
            jsonObject.put("prepayId", prepay_id);
            if(trade_type.equals("APP")) {
                jsonObject.put("packageValue", "Sign=WXPay");
            }else{
                jsonObject.put("packageValue", packages);
            }
            jsonObject.put("sign", finalSign);
            jsonObject.put("orderId", orderId);
            jsonObject.put("productName", productName);
            jsonObject.put("total", total);
            jsonObject.put("codeUrl", code_url);
            jsonObject.put("mwebUrl", mweb_url);
        }
        System.out.println(" PayUnifiedorder result "+jsonObject.toJSONString());
        return jsonObject;
    }

    private String createSign(SortedMap<String, String> packageParams, String partnerkey) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!StringUtils.isEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + partnerkey);
        System.out.println(" 签名串："+sb.toString());
        String sign = Md5.api.MD5Encode(sb.toString());
        return sign;
    }

    private Map<String, String> doXMLParse(String strxml) throws Exception {
        if (StringUtils.isEmpty(strxml)) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        InputStream in = String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }
            map.put(k, v);
        }
        if (null != in) {
            in.close();
        }
        return map;
    }

    private InputStream String2Inputstream(String str) {
        try {
            return new ByteArrayInputStream(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

}
