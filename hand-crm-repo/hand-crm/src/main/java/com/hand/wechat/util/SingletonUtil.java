package com.hand.wechat.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 微信Token和Ticket
 */
public class SingletonUtil {

    public static final SingletonUtil api = new SingletonUtil();
    private SingletonUtil() {}

    public static SingletonUtil getInstancei() {
        return api;
    }

    /**
     * 设置Token
     * @param redisTemplate
     * @param accessTokenKey
     * @param accessToken
     * @param seconds
     */
    public void setAccessToken(StringRedisTemplate redisTemplate, String accessTokenKey, String accessToken, long seconds) {
        redisTemplate.opsForValue().set(accessTokenKey, accessToken, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取Token的值
     * @param redisTemplate
     * @param accessTokenKey
     * @return
     */
    public String getAccessToken(StringRedisTemplate redisTemplate, String accessTokenKey) {
        return redisTemplate.opsForValue().get(accessTokenKey);
    }

    /**
     * 获取Token过期时间
     * @param redisTemplate
     * @param accessTokenKey
     * @return
     */
    public long getTokenExpires(StringRedisTemplate redisTemplate, String accessTokenKey) {
        return redisTemplate.getExpire(accessTokenKey, TimeUnit.SECONDS);
    }

    /**
     * 设置Ticket
     * @param redisTemplate
     * @param jsapiTicketKey
     * @param jsapiTicket
     * @param seconds
     */
    public void setJsapiTicket(StringRedisTemplate redisTemplate, String jsapiTicketKey, String jsapiTicket, long seconds) {
        redisTemplate.opsForValue().set(jsapiTicketKey, jsapiTicket, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取Ticket的值
     * @param redisTemplate
     * @param jsapiTicketKey
     * @return
     */
    public String getJsapiTicket(StringRedisTemplate redisTemplate, String jsapiTicketKey) {
        return redisTemplate.opsForValue().get(jsapiTicketKey);
    }

    /**
     * 获取Ticket过期时间
     * @param redisTemplate
     * @param jsapiTicketKey
     * @return
     */
    public long getTicketExpires(StringRedisTemplate redisTemplate, String jsapiTicketKey) {
        return redisTemplate.getExpire(jsapiTicketKey, TimeUnit.SECONDS);
    }

    /**
     * 设置Token（小程序）
     * @param redisTemplate
     * @param accessTokenKey
     * @param accessToken
     * @param seconds
     */
    public void setAccessTokenMini(StringRedisTemplate redisTemplate, String accessTokenKey, String accessToken, long seconds) {
        redisTemplate.opsForValue().set(accessTokenKey, accessToken, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取Token的值（小程序）
     * @param redisTemplate
     * @param accessTokenKey
     * @return
     */
    public String getAccessTokenMini(StringRedisTemplate redisTemplate, String accessTokenKey) {
        return redisTemplate.opsForValue().get(accessTokenKey);
    }

    /**
     * 获取Token过期时间（小程序）
     * @param redisTemplate
     * @param accessTokenKey
     * @return
     */
    public long getTokenExpiresMini(StringRedisTemplate redisTemplate, String accessTokenKey) {
        return redisTemplate.getExpire(accessTokenKey, TimeUnit.SECONDS);
    }

}
