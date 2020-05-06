package com.hand.wechat.util;

import com.alibaba.fastjson.JSONObject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 查询订单
 */
public class PayOrderquery {

    public static final PayOrderquery api = new PayOrderquery();
    private PayOrderquery(){}

    public JSONObject orderquery(Environment env,
                                 String appID, String partner, String partnerKey,
                                 String outTradeNo) {
        JSONObject jsonObject = new JSONObject();

        String appid = env.getProperty(appID);
        String mch_id = env.getProperty(partner);
        String partnerkey = env.getProperty(partnerKey);
        String nonce_str = UUID.randomUUID().toString().replace("-", "");

        OutputStream outputStream = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        final StringBuffer stringBuffer = new StringBuffer();
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("out_trade_no", outTradeNo);
        String sign = createSign(packageParams, partnerkey);

        try {
            String UTF8 = "UTF-8";
            String reqBody =
                    "<xml>" +
                            "<appid>" + appid + "</appid>" +
                            "<mch_id>" + mch_id + "</mch_id>" +
                            "<nonce_str>" + nonce_str + "</nonce_str>" +
                            "<out_trade_no>" + outTradeNo + "</out_trade_no>" +
                            "<sign>" + sign + "</sign>";
            reqBody += "</xml>";
            URL httpUrl = new URL("https://api.mch.weixin.qq.com/pay/orderquery");
            HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
            httpURLConnection.setRequestProperty("Host", "api.mch.weixin.qq.com");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10 * 1000);
            httpURLConnection.setReadTimeout(10 * 1000);
            httpURLConnection.connect();
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(reqBody.getBytes(UTF8));
            System.out.println(" PayOrderquery reqBody "+reqBody);
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
        System.out.println(" PayOrderquery resp "+resp);
        try {
            Map<String, String> xmlMap = doXMLParse(resp);
            String return_code = xmlMap.get("return_code");
            String result_code = xmlMap.get("result_code");
            String trade_state = xmlMap.get("trade_state");
            if ((!StringUtils.isEmpty(return_code) && return_code.equals("SUCCESS"))
                    && (!StringUtils.isEmpty(result_code) && result_code.equals("SUCCESS"))
                    && (!StringUtils.isEmpty(trade_state) && trade_state.equals("SUCCESS"))) {
                jsonObject.put("errcode", "0");
                jsonObject.put("errmsg", "此订单已支付");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!jsonObject.containsKey("errcode")) {
            jsonObject.put("errcode", "1");
            jsonObject.put("errmsg", "此订单未支付");
        }
        System.out.println(" PayOrderquery result "+jsonObject.toJSONString());
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
