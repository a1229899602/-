package com.hand.wechat.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeixinContent {

    public static final WeixinContent api = new WeixinContent();
    private WeixinContent() {}

    /**
     * 长链接转短链接
     *
     * @param longUrl
     * @return
     */
    public String shorturlContent(String longUrl) {
        JSONObject result = new JSONObject();
        result.put("action", "long2short");
        result.put("long_url", longUrl);
        return result.toJSONString();
    }

    /**
     * 带场景永久二维码
     *
     * @param content
     * @return
     */
    public String qrcodeCreateContent(String content, int seconds) {
        JSONObject result = new JSONObject();
        result.put("expire_seconds", seconds);
        result.put("action_name", "QR_STR_SCENE");
        JSONObject action_info = new JSONObject();
        JSONObject scene = new JSONObject();
        scene.put("scene_str", content);
        action_info.put("scene", scene);
        result.put("action_info", action_info);
        return result.toString();
    }

    /**
     * 带场景永久二维码
     *
     * @param content
     * @return
     */
    public String qrcodeCreateContent(String content) {
        JSONObject result = new JSONObject();
        result.put("action_name", "QR_LIMIT_STR_SCENE");
        JSONObject action_info = new JSONObject();
        JSONObject scene = new JSONObject();
        scene.put("scene_str", content);
        action_info.put("scene", scene);
        result.put("action_info", action_info);
        return result.toString();
    }

    /**
     * 模板消息
     *
     * @param openid
     * @param templateId
     * @param url
     * @param datas
     * @return
     */
    public String messageTemplateSendContent(String openid, String templateId, String url, List<String> datas) {
        JSONObject result = new JSONObject();
        result.put("url", url);
        result.put("touser", openid);
        result.put("template_id", templateId);
        Map<String, Object> kv = new HashMap<String, Object>();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, Object> knval = new HashMap<String, Object>();
            String itemName = "keyword" + i;
            if (0 == i) {
                itemName = "first";
            } else if (i == datas.size() - 1) {
                itemName = "remark";
            }
            knval.put("value", datas.get(i));
            knval.put("color", "#173177");
            kv.put(itemName, knval);
        }
        result.put("data", kv);
        return result.toJSONString();
    }

    /**
     * 客服消息发送失败后，转为发送模板消息
     *
     * @param env
     * @param openid
     * @param content
     * @param url
     * @return
     */
    public String defaultMessageTemplateSendContent(Environment env, String openid, String content, String url) {
        JSONObject result = new JSONObject();
        result.put("url", url);
        result.put("touser", openid);
        result.put("template_id", env.getProperty("custom.template"));
        Map<String, Object> kv = new HashMap<String, Object>();

        Map<String, Object> knvalFirst = new HashMap<String, Object>();
        knvalFirst.put("value", "您好，您有新的客服消息");
        knvalFirst.put("color", "#173177");
        kv.put("first", knvalFirst);

        Map<String, Object> knvalKeyword1 = new HashMap<String, Object>();
        knvalKeyword1.put("value", "客服消息");
        knvalKeyword1.put("color", "#173177");
        kv.put("keyword1", knvalKeyword1);

        Map<String, Object> knvalKeyword2 = new HashMap<String, Object>();
        knvalKeyword2.put("value", content);
        knvalKeyword2.put("color", "#173177");
        kv.put("keyword2", knvalKeyword2);

        if(!StringUtils.isEmpty(url)){
            Map<String, Object> knvalRemark = new HashMap<String, Object>();
            knvalRemark.put("value", "点击查看详情");
            knvalRemark.put("color", "#173177");
            kv.put("remark", knvalRemark);
        }

        result.put("data", kv);
        return result.toJSONString();
    }


    /**
     * 订单状态模板消息
     * @param env
     * @param openid
     * @param orderCode
     * @param orderStatus
     * @param url
     * @return
     */
    public String orderStatusMessageTemplate(Environment env, String openid,
                                             String orderCode, String orderStatus, String url) {
        JSONObject result = new JSONObject();
        result.put("url", url);
        result.put("touser", openid);
        result.put("template_id", env.getProperty("order.template"));
        Map<String, Object> kv = new HashMap<>();

        Map<String, Object> knvalFirst = new HashMap<>();
        knvalFirst.put("value", "小主，您的订单有新动态");
        knvalFirst.put("color", "#173177");
        kv.put("first", knvalFirst);

        Map<String, Object> knvalKeyword1 = new HashMap<>();
        knvalKeyword1.put("value", orderCode);
        knvalKeyword1.put("color", "#173177");
        kv.put("OrderSn", knvalKeyword1);

        Map<String, Object> knvalKeyword2 = new HashMap<>();
        knvalKeyword2.put("value", orderStatus);
        knvalKeyword2.put("color", "#173177");
        kv.put("OrderStatus", knvalKeyword2);

        if(!StringUtils.isEmpty(url)){
            Map<String, Object> knvalRemark = new HashMap<String, Object>();
            knvalRemark.put("value", "祝您购物快乐，常来店逛逛！");
            knvalRemark.put("color", "#173177");
            kv.put("remark", knvalRemark);
        }

        result.put("data", kv);
        return result.toJSONString();
    }
    /**
     * 客服消息-图文消息
     *
     * @param openid
     * @param imgTexts
     * @return
     */
    public String messageCustomSendImgTextContent(String openid, List<ImgText> imgTexts) {
        JSONObject result = new JSONObject();
        result.put("touser", openid);
        result.put("msgtype", "news");
        JSONObject jsonObject = new JSONObject();
        List<Map<String, String>> maps = new ArrayList<>();
        for (ImgText imgText: imgTexts) {
            Map<String, String> map = new HashMap<>();
            map.put("title", imgText.getTitle());
            map.put("description", imgText.getDescription());
            map.put("url", imgText.getUrl());
//          map.put("picurl", FileServiceUtils.computePublicFileUrl(imgText.getPicurl()));
            maps.add(map);
        }
        jsonObject.put("articles", maps);
        result.put("news", jsonObject);
        return result.toString();
    }

    /**
     * 客服消息-文本消息
     *
     * @param openid
     * @param content
     * @return
     */
    public String messageCustomSendContent(String openid, String content) {
        JSONObject result = new JSONObject();
        result.put("touser", openid);
        result.put("msgtype", "text");
        JSONObject jsonObject1 = new JSONObject();
        if(content.indexOf("href=") > -1 && content.indexOf("</a>") > -1 && content.indexOf("href=") < content.indexOf("</a>")) {
            String sourceStr = content.substring(content.indexOf("href="), content.indexOf("</a>"));
            String targetStr = sourceStr.replace(" ", "");
            content = content.replace(sourceStr, targetStr);
        }
        jsonObject1.put("content", content);
        result.put("text", jsonObject1);
        return JSON.toJSONString(result);
    }

}
