package com.hand.hm.access.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 是否有分享
     */
    private String isShare;

    /**
     * 是否通过分享参与
     */
    private String isJoinByShare;

    /**
     * 已签到的天数
     */
    private String signedInDays;


    public UserInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public String getIsJoinByShare() {
        return isJoinByShare;
    }

    public void setIsJoinByShare(String isJoinByShare) {
        this.isJoinByShare = isJoinByShare;
    }

    public String getSignedInDays() {
        return signedInDays;
    }

    public void setSignedInDays(String signedInDays) {
        this.signedInDays = signedInDays;
    }

}