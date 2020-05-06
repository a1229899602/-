package com.hand.wechat;


import com.alibaba.fastjson.JSONObject;
import com.hand.wechat.util.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.DataOutput;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;


/**
 * 服务号微信接口
 */
public class FuwuService {

    public static final FuwuService api = new FuwuService();

    public FuwuService() {
    }

    private final String appID = "fuwu.AppID";
    private final String appSecret = "fuwu.AppSecret";
    private final String partner = "fuwu.partner";
    private final String partnerKey = "fuwu.partnerKey";
    private final String accessTokenKey = "fuwuAccessTokenKey";
    private final String jsapiTicketKey = "fuwuJsapiTicketKey";
    private final String trade_type = "JSAPI";
    private final String trade_type_NATIVE = "NATIVE";
    private final String trade_type_MWEB = "MWEB";
    private final String pkcs12Path = "fuwu.pkcs12Path";


    @ApiOperation("查询订单支付状态")
    public JSONObject payOrderquery(Environment env, String outTradeNo) {
        return PayOrderquery.api.orderquery(env, appID, partner, partnerKey, outTradeNo);
    }

    @ApiOperation("统一下单签名")
    public JSONObject payUnifiedorder(Environment env, HttpServletRequest request,
                                      String openid, String orderId, String productName, String total, String outTradeNo) {
        return PayUnifiedorder.api.unifiedorder(env, request, trade_type, appID, partner, partnerKey, orderId, productName, total, outTradeNo, openid, null, null);
    }

    @ApiOperation("统一下单签名-扫码支付")
    public JSONObject payUnifiedorderNATIVE(Environment env, HttpServletRequest request,
                                            String orderId, String productName, String total, String outTradeNo) {
        return PayUnifiedorder.api.unifiedorder(env, request, trade_type_NATIVE, appID, partner, partnerKey, orderId, productName, total, outTradeNo, null, null, null);
    }

    @ApiOperation("统一下单签名-H5支付")
    public JSONObject payUnifiedorderH5(Environment env, HttpServletRequest request,
                                        String orderId, String productName, String total, String outTradeNo, String scene_info, String ip) {
        return PayUnifiedorder.api.unifiedorder(env, request, trade_type_MWEB, appID, partner, partnerKey, orderId, productName, total, outTradeNo, null, scene_info, ip);
    }

    @ApiOperation("jssdk签名")
    public JSONObject jssdkSign(Environment env, StringRedisTemplate redisTemplate, String url) {
        JSONObject result = new JSONObject();
        String noncestr = UUID.randomUUID().toString().replace("-", "");
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序的
        String signStr = "jsapi_ticket=" + getJsapiTicket(env, redisTemplate) + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        System.out.println(" jssdk签名：" + signStr);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(signStr.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result.put("nonceStr", noncestr);
        result.put("timestamp", timestamp);
        result.put("signature", signature);
        result.put("appId", env.getProperty(appID));
        return result;
    }

    @ApiOperation("byte转换")
    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    @ApiOperation("用户信息")
    public JSONObject userInfo(Environment env, StringRedisTemplate redisTemplate, String openid) {
        String sendUrl = WeixinUrl.api.userInfoUrl(getAccessToken(env, redisTemplate), openid);
        return WeixinHttp.api.httpsRequest(sendUrl, WeixinHttp.GET);
    }

    @ApiOperation("通过code换取access_token、refresh_token和已授权scope")
    public JSONObject snsOauth2AccessToken(Environment env, String code) {
        String sendUrl = WeixinUrl.api.snsOauth2AccessTokenUrl(env.getProperty(appID), env.getProperty(appSecret), code);
        return WeixinHttp.api.httpsRequest(sendUrl, WeixinHttp.GET);
    }

    @ApiOperation("生成Ticket")
    public String getJsapiTicket(Environment env, StringRedisTemplate redisTemplate) {
        String jsapiTicket = SingletonUtil.getInstancei().getJsapiTicket(redisTemplate, jsapiTicketKey);
        if (StringUtils.isEmpty(jsapiTicket)) {
            String ticketUrl = WeixinUrl.api.ticketGetticketUrl(getAccessToken(env, redisTemplate));
            JSONObject jsonObject = WeixinHttp.api.httpsRequest(ticketUrl, WeixinHttp.GET);
            if (null != jsonObject && jsonObject.containsKey("errcode") && jsonObject.getInteger("errcode") == 0) {
                jsapiTicket = jsonObject.getString("ticket");
                int expires_in = (jsonObject.getInteger("expires_in") - 200);
                SingletonUtil.getInstancei().setJsapiTicket(redisTemplate, jsapiTicketKey, jsapiTicket, expires_in);
                System.out.println(" 生成ticket成功：" + jsapiTicket);
            } else {
                System.out.println(" 生成ticket失败：" + ((null == jsonObject) ? "null" : jsonObject.toString()));
            }
        }
        return jsapiTicket;
    }

    @ApiOperation("生成Token")
    public String getAccessToken(Environment env, StringRedisTemplate redisTemplate) {
        String accessToken = SingletonUtil.getInstancei().getAccessToken(redisTemplate, accessTokenKey);
        if (StringUtils.isEmpty(accessToken)) {
            String tokenUrl = WeixinUrl.api.tokenUrl(env.getProperty(appID), env.getProperty(appSecret));
            JSONObject jsonObject = WeixinHttp.api.httpsRequest(tokenUrl, WeixinHttp.GET);
            if (null != jsonObject && !jsonObject.containsKey("errcode")) {
                accessToken = jsonObject.getString("access_token");
                long expires_in = (jsonObject.getLong("expires_in") - 200);
                SingletonUtil.getInstancei().setAccessToken(redisTemplate, accessTokenKey, accessToken, expires_in);
                System.out.println(" 生成token成功：" + accessToken);
            } else {
                System.out.println(" 生成Token失败：" + ((null == jsonObject) ? "null" : jsonObject.toString()));
            }
        }
        return accessToken;
    }

    @ApiOperation("重置Token和Ticket")
    public void reset(Environment env, StringRedisTemplate redisTemplate) {
        redisTemplate.delete(accessTokenKey);
        redisTemplate.delete(jsapiTicketKey);
        getAccessToken(env, redisTemplate);
        getJsapiTicket(env, redisTemplate);
    }

    @ApiOperation("字符串转数字")
    private int parseInt(String str, int defaul) {
        try {
            if (!StringUtils.isEmpty(str)) {
                defaul = Integer.parseInt(str);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaul;
    }

    @ApiOperation("uri转义")
    private String getEncodeUri(String redirect_uri) {
        try {
            return URLEncoder.encode(redirect_uri, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @ApiOperation("客服消息-文本消息（svc内部调用）")
    public JSONObject messageCustomSendTextSvc(Environment env, StringRedisTemplate redisTemplate, String userId, String content, String url) {
        Assert.notNull(userId, "userId不能为空");
        Assert.notNull(content, "content不能为空");
        String openid = redisTemplate.opsForValue().get("FuWuOpenid" + userId);
//        Assert.notNull(openid, "用户无微信授权");
        System.out.println("用户无微信授权");
        JSONObject jsonObject = messageCustomSendText(env, redisTemplate, openid, content, url);
//        Assert.isTrue(jsonObject.getString("errcode").equals("0"), "文本消息发送失败");
        return jsonObject;
    }

    @ApiOperation("客服消息-文本消息")
    public JSONObject messageCustomSendText(Environment env, StringRedisTemplate redisTemplate, String openid, String content, String url) {
        String sendUrl = WeixinUrl.api.messageCustomSendUrl(getAccessToken(env, redisTemplate));
        String outputStr = WeixinContent.api.messageCustomSendContent(openid, content);
        JSONObject jsonObject = WeixinHttp.api.httpsRequest(sendUrl, WeixinHttp.GET, outputStr);
        if (jsonObject.containsKey("errcode") && !jsonObject.getString("errcode").equals("0")) {
            // 客服消息发送失败，转为发送模板消息
            if (content.indexOf("href") > -1) {
                int index = (content.indexOf("href"));
                if (index > 3) {
                    content = content.substring(0, index - 3);
                }
            }
            sendUrl = WeixinUrl.api.messageTemplateSendUrl(getAccessToken(env, redisTemplate));
            outputStr = WeixinContent.api.defaultMessageTemplateSendContent(env, openid, content, url);
            jsonObject = WeixinHttp.api.httpsRequest(sendUrl, WeixinHttp.GET, outputStr);
        }
        return jsonObject;
    }

    @ApiOperation("微信推送-订单状态更新推送")
    public JSONObject orderStatusSendText(Environment env, StringRedisTemplate redisTemplate, String openid,
                                          String orderCode, String orderStatus, String url) {
        String sendUrl = WeixinUrl.api.messageTemplateSendUrl(getAccessToken(env, redisTemplate));
        String outputStr = WeixinContent.api.orderStatusMessageTemplate(env, openid, orderCode, orderStatus, url);
        JSONObject jsonObject = WeixinHttp.api.httpsRequest(sendUrl, WeixinHttp.GET, outputStr);
        return jsonObject;
    }


}
