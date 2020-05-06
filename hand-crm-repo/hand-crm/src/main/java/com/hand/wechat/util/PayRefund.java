package com.hand.wechat.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.core.env.Environment;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 退款
 */
public class PayRefund {

    public static final PayRefund api = new PayRefund();
    private PayRefund(){}

    public JSONObject refund(Environment env, String pkcs12Path, String appID, String partner, String partnerKey, String outTradeNo, String totalFee, String refundFee) {
        JSONObject jsonObject = new JSONObject();
        String nonce_str = UUID.randomUUID().toString().replace("-", "");
        String out_refund_no = UUID.randomUUID().toString().replace("-", "");
        String appid = env.getProperty(appID);
        String mch_id = env.getProperty(partner);
        String partnerkey = env.getProperty(partnerKey);
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("out_trade_no", outTradeNo);
        packageParams.put("out_refund_no", out_refund_no);
        packageParams.put("total_fee", totalFee);
        packageParams.put("refund_fee", refundFee);
        String sign = createSign(packageParams, partnerkey);
        String xml = "<xml>"
                + "<appid>" + appid + "</appid>"
                + "<mch_id>" + mch_id + "</mch_id>"
                + "<nonce_str>" + nonce_str + "</nonce_str>"
                + "<out_trade_no>" + outTradeNo + "</out_trade_no>"
                + "<out_refund_no>" + out_refund_no + "</out_refund_no>"
                + "<refund_fee>" + refundFee + "</refund_fee>"
                + "<total_fee>" + totalFee + "</total_fee>"
                + "<sign>" + sign + "</sign>"
                + "</xml>";

        KeyStore keyStore = null;
        FileInputStream instream = null;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
            instream = new FileInputStream(new File(env.getProperty(pkcs12Path, "")));
            keyStore.load(instream, mch_id.toCharArray());
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null != instream) {
                try {
                    instream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String jsonStr = "";
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            HttpPost httPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund"); // 设置响应头信息
            httPost.addHeader("Connection", "keep-alive");
            httPost.addHeader("Accept", "*/*");
            httPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httPost.addHeader("Host", "api.mch.weixin.qq.com");
            httPost.addHeader("X-Requested-With", "XMLHttpRequest");
            httPost.addHeader("Cache-Control", "max-age=0");
            httPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httPost.setEntity(new StringEntity(xml, "UTF-8"));
            response = httpclient.execute(httPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map map = doXMLParse(jsonStr);
            if ("success".equalsIgnoreCase((String) map.get("return_code"))) {
                if ("success".equalsIgnoreCase((String) map.get("result_code"))) {
                    jsonObject.put("errcode", "0");
                    jsonObject.put("errmsg", "退款成功");
                }
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != httpclient){
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!jsonObject.containsKey("errcode")){
            jsonObject.put("errcode", "1");
            jsonObject.put("errmsg", "退款失败");
            System.out.println(jsonStr);
        }
        return jsonObject;
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



}
