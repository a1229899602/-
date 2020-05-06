package com.hand.wechat;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Api(value = "app/we-chat", description = "app应用微信接口", tags = {"app-we-chat"})
@RequestMapping(value = "app/we-chat", produces = {"application/json"})
@RestController
public class WeChatController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Environment env;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    protected Environment environment;

    public static final FuwuService api = new FuwuService();

    /*

    @ApiOperation("查询订单支付状态")
    @RequestMapping(value = "payOrderQuery", method = {RequestMethod.GET})
    public JSONObject payOrderQuery(HttpServletRequest request, @RequestParam("outTradeNo") String outTradeNo) {
        Assert.notNull(outTradeNo, "outTradeNo不能为空");
        JSONObject jsonObject = WeChatAppService.api.payOrderQuery(env, outTradeNo);
        return jsonObject;
    }*/

    /*
    @ApiOperation("统一下单签名")
    @RequestMapping(value = "payUnifiedOrder", method = {RequestMethod.GET})
    public JSONObject payUnifiedOrder(HttpServletRequest request,
                                      @ApiParam(value = "订单code ", required = true)
                                      @RequestParam("orderCode") String orderCode,
                                      @ApiParam(value = "支付页面显示的支付商品名称", required = true)
                                      @RequestParam("productName") String productName,
                                      @ApiParam(value = "支付金额", required = true)
                                      @RequestParam("total") String total,
                                      @ApiParam(value = "商户订单号 ", required = true)
                                      @RequestParam("outTradeNo") String outTradeNo) {
        Assert.notNull(orderCode, "orderId不能为空");
        Assert.notNull(productName, "productName不能为空");
        Assert.notNull(total, "total不能为空");
        JSONObject jsonObject = WeChatAppService.api.payUnifiedOrder(env, request, orderCode, productName, total, outTradeNo, null);
        Assert.isTrue(jsonObject.getString("errcode").equals("0"), "统一下单签名失败");
        return jsonObject;
    }*/

/*
    @ApiOperation("获取用户个人信息")
    @RequestMapping(value = "snsUserInfo", method = {RequestMethod.GET})
    public JSONObject snsUserInfo(@RequestParam("accessToken") String accessToken, @RequestParam("openid") String openid) {
        Assert.notNull(accessToken, "accessToken不能为空");
        Assert.notNull(openid, "openid不能为空");
        JSONObject jsonObject = WeChatAppService.api.userInfo(accessToken, openid);
        Assert.isTrue(jsonObject.containsKey("unionid"), "accessToken或openid无效");
        return jsonObject;
    }*/


    @ApiOperation("根据openId获取用户信息")
    @RequestMapping(value = "getUserInfoByOpenId", method = {RequestMethod.GET})
    public JSONObject getUserInfoByOpenId(@RequestParam("openid") String openid) {
        Assert.notNull(openid, "openid不能为空");
        JSONObject jsonObject = FuwuService.api.userInfo(env, redisTemplate, openid);
        Assert.isTrue(jsonObject.containsKey("unionid"), "openid无效");
        return jsonObject;
    }
/*
    @ApiOperation("刷新或续期access_token使用")
    @RequestMapping(value = "snsRefreshToken", method = {RequestMethod.GET})
    public JSONObject snsRefreshToken(@RequestParam("refreshToken") String refreshToken) {
        Assert.notNull(refreshToken, "refreshToken不能为空");
        JSONObject jsonObject = WeChatAppService.api.snsOauth2RefreshToken(env, refreshToken);
        Assert.isTrue(jsonObject.containsKey("access_token"), "refreshToken失效");
        return jsonObject;
    }

    @ApiOperation("检查access_token有效性")
    @RequestMapping(value = "snsAuth", method = {RequestMethod.GET})
    public JSONObject snsAuth(@RequestParam("accessToken") String accessToken, @RequestParam("openid") String openid) {
        Assert.notNull(accessToken, "accessToken不能为空");
        Assert.notNull(openid, "openid不能为空");
        JSONObject jsonObject = WeChatAppService.api.snsAuth(accessToken, openid);
        Assert.isTrue(jsonObject.getString("errcode").equals("0"), "accessToken失效");
        return jsonObject;
    }
*/

//    @ApiOperation("统一下单签名-H5支付")
//    @RequestMapping(value = "payUnifiedorderH5", method = {RequestMethod.GET})
//    public DataOutput<JSONObject> payUnifiedorderH5(HttpServletRequest request, @RequestParam("orderId") String orderId, @RequestParam("productName") String productName, @RequestParam("total") String total, @RequestParam("outTradeNo") String outTradeNo, @RequestParam("ip") String ip) {
//        Assert.notNull(orderId, "orderId不能为空");
//        Assert.notNull(productName, "productName不能为空");
//        Assert.notNull(total, "total不能为空");
//        Assert.notNull(ip, "ip不能为空");
//        String scene_info = "{\"h5_info\": \"h5_info\",{\"type\": \"h5pay\",\"wap_url\": \"http://www.doctorup.cn\",\"wap_name\": \"百诺名医汇\"}}";
//        JSONObject jsonObject = FuwuService.api.payUnifiedorderH5(env, request, orderId, productName, total, outTradeNo, scene_info, ip);
//        Assert.isTrue(jsonObject.getString("errcode").equals("0"), "H5支付统一下单签名失败");
//        return new DataOutput<>(jsonObject);
//    }


    @ApiOperation("请求微信用户获得授权")
    @RequestMapping(value = "oauthUserInfo", method = {RequestMethod.GET})
    public void oauthUserInfo(@RequestParam("sourceUrl") String sourceUrl, HttpServletResponse response) throws ServletException, IOException {
        String redirectUri = env.getProperty("domainName") + env.getProperty("fuwu.oauth.notifyUrl")
                + "?sourceUrl=" + sourceUrl;
        redirectUri = getEncodeUri(redirectUri);
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" + env.getProperty("fuwu.AppID") +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=123" +
                "#wechat_redirect";
        response.sendRedirect(url);
    }

    @ApiOperation("微信静默授权")
    @RequestMapping(value = "oauth", method = {RequestMethod.GET})
    public void oauth(@RequestParam("sourceUrl") String sourceUrl, HttpServletResponse response) throws IOException {
        String redirectUri = env.getProperty("domainName") + env.getProperty("fuwu.oauth.notifyUrl")
                + "?sourceUrl=" + sourceUrl;
        redirectUri = getEncodeUri(redirectUri);
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" + env.getProperty("fuwu.AppID") +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=snsapi_base" +
                "&state=123" +
                "#wechat_redirect";
        response.sendRedirect(url);
    }

    @ApiOperation("jsSdk签名")
    @RequestMapping(value = "jsSdkSign", method = {RequestMethod.GET})
    public JSONObject jsSdkSign(@RequestParam("url") String url, HttpServletResponse response) {
        Assert.notNull(url, "url不能为空");
        JSONObject jsonObject = FuwuService.api.jssdkSign(env, redisTemplate, url);
        //该接口设置不缓存
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        return jsonObject;
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


    private String getStrFromRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
