package com.hand.wechat.util;

/**
 * 微信接口地址
 */
public class WeixinUrl {

    public static final WeixinUrl api = new WeixinUrl();
    private WeixinUrl() {}

    private final String media_get = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
    private final String shorturl = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=%s";
    private final String qrcode_create = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
    private final String message_template_send = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    private final String message_custom_send = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
    private final String menu_create = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    private final String menu_get = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";
    private final String user_info = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
    private final String ticket_getticket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
    private final String token = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    private final String sns_userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
    private final String sns_auth = "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s";
    private final String sns_oauth2_refresh_token = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    private final String sns_oauth2_access_token = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private final String sns_jscode2session = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    private final static String authorize_url = "https://open.weixin.qq.com/connect/oauth2/authorize?";


    public String authorizeUrl(String appId,String redirectUri,String responseType,String scope,String state){
        return String.format(authorize_url,appId,redirectUri,responseType,scope,state);

    }

    /**
     * 微信媒体文件地址
     *
     * @param token
     * @param mediaId
     * @return
     */
    public String mediaGetUrl(String token, String mediaId) {
        return String.format(media_get, token, mediaId);
    }

    /**
     * 长链接转短链接
     *
     * @param token
     * @return
     */
    public String shorturlUrl(String token) {
        return String.format(shorturl, token);
    }

    /**
     * 带场景二维码
     *
     * @param token
     * @return
     */
    public String qrcodeCreateUrl(String token) {
        return String.format(qrcode_create, token);
    }

    /**
     * 模板消息
     *
     * @param token
     * @return
     */
    public String messageTemplateSendUrl(String token) {
        return String.format(message_template_send, token);
    }

    /**
     * 客服消息
     *
     * @param token
     * @return
     */
    public String messageCustomSendUrl(String token) {
        return String.format(message_custom_send, token);
    }

    /**
     * 创建公众号菜单
     *
     * @param token
     * @return
     */
    public String menuCreateUrl(String token) {
        return String.format(menu_create, token);
    }

    /**
     * 查询公众号菜单
     *
     * @param token
     * @return
     */
    public String menuGetUrl(String token) {
        return String.format(menu_get, token);
    }

    /**
     * 用户信息
     *
     * @param token
     * @param openid
     * @return
     */
    public String userInfoUrl(String token, String openid) {
        return String.format(user_info, token, openid);
    }

    /**
     * ticket
     *
     * @param token
     * @return
     */
    public String ticketGetticketUrl(String token) {
        return String.format(ticket_getticket, token);
    }

    /**
     * token
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public String tokenUrl(String appId, String appSecret) {
        return String.format(token, appId, appSecret);
    }


    /**
     * 获取用户个人信息
     *
     * @param accessToken
     * @param openid
     * @return
     */
    public String snsUserinfoUrl(String accessToken, String openid) {
        return String.format(sns_userinfo, accessToken, openid);
    }

    /**
     * 检查access_token有效性
     *
     * @param accessToken
     * @param openid
     * @return
     */
    public String snsAuthUrl(String accessToken, String openid) {
        return String.format(sns_auth, accessToken, openid);
    }

    /**
     * 刷新或续期access_token使用
     *
     * @param appid
     * @param refreshToken
     * @return
     */
    public String snsOauth2RefreshTokenUrl(String appid, String refreshToken) {
        return String.format(sns_oauth2_refresh_token, appid, refreshToken);
    }

    /**
     * 通过code换取access_token、refresh_token和已授权scope
     *
     * @param appId
     * @param appSecret
     * @param code
     * @return
     */
    public String snsOauth2AccessTokenUrl(String appId, String appSecret, String code) {
        return String.format(sns_oauth2_access_token, appId, appSecret, code);
    }

    /**
     * 通过code换取open_id和session_key（小程序）
     *
     * @param appId
     * @param appSecret
     * @param code
     * @return
     */
    public String snsJscode2session(String appId, String appSecret, String code) {
        return String.format(sns_jscode2session, appId, appSecret, code);
    }

}
