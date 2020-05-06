package com.hand.wechat;

import com.alibaba.fastjson.JSONObject;
import com.hand.wechat.util.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;


/**
 * app应用微信接口
 */
public class WeChatAppService {

    public static final WeChatAppService api = new WeChatAppService();
    private WeChatAppService(){}

    private final String appID = "app.AppID";
    private final String appSecret = "app.AppSecret";
    private final String partner = "app.partner";
    private final String partnerKey = "app.partnerKey";
    private final String trade_type = "JSAPI";
    private final String pkcs12Path = "app.pkcs12Path";

    @ApiOperation("退款")
    public JSONObject payRefund(Environment env, String outTradeNo, String totalFee, String refundFee) {
        return PayRefund.api.refund(env, pkcs12Path, appID, partner, partnerKey, outTradeNo, totalFee, refundFee);
    }

    @ApiOperation("查询订单支付状态")
    public JSONObject payOrderQuery(Environment env, String outTradeNo) {
        return PayOrderquery.api.orderquery(env, appID, partner, partnerKey, outTradeNo);
    }

    @ApiOperation("统一下单签名")
    public JSONObject payUnifiedOrder(Environment env, HttpServletRequest request,
                                      String orderId, String productName, String total, String outTradeNo, String openId) {
        return PayUnifiedorder.api.unifiedorder(env, request, trade_type, appID, partner, partnerKey, orderId, productName, total, outTradeNo, openId, null, null);
    }

    @ApiOperation("获取用户个人信息")
    public JSONObject userInfo(String accessToken, String openid) {
        String sendUrl = WeixinUrl.api.snsUserinfoUrl(accessToken, openid);
        return WeixinHttp.api.httpsRequest(sendUrl, WeixinHttp.GET);
    }

    @ApiOperation("检查access_token有效性")
    public JSONObject snsAuth(String accessToken, String openid) {
        String sendUrl = WeixinUrl.api.snsAuthUrl(accessToken, openid);
        return WeixinHttp.api.httpsRequest(sendUrl, WeixinHttp.GET);
    }

    @ApiOperation("刷新或续期access_token使用")
    public JSONObject snsOauth2RefreshToken(Environment env, String refreshToken) {
        String sendUrl = WeixinUrl.api.snsOauth2RefreshTokenUrl(env.getProperty(appID), refreshToken);
        return WeixinHttp.api.httpsRequest(sendUrl, WeixinHttp.GET);
    }

    @ApiOperation("通过code换取access_token、refresh_token和已授权scope")
    public JSONObject snsOauth2AccessToken(Environment env, String code) {
        String sendUrl = WeixinUrl.api.snsOauth2AccessTokenUrl(env.getProperty(appID), env.getProperty(appSecret), code);
        return WeixinHttp.api.httpsRequest(sendUrl, WeixinHttp.GET);
    }

}
