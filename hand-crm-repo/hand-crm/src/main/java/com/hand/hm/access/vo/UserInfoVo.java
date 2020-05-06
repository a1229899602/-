package com.hand.hm.access.vo;

import io.swagger.annotations.ApiModelProperty;

public class UserInfoVo {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 是否有分享
     */
    @ApiModelProperty("是否有分享")
    private String isShare;

    /**
     * 是否通过分享参与
     */
    @ApiModelProperty("是否通过分享参与")
    private String isJoinByShare;

    /**
     * 已签到的天数
     */
    @ApiModelProperty("已签到的天数 不是连续的 ")
    private String signedInDays;

    @ApiModelProperty("今天是否打卡  true  false")
    private Boolean signIn;

    @ApiModelProperty("总打卡人数")
    private Integer sumUser;

    @ApiModelProperty("连续打卡天数")
    private Integer sustain;

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

    public Boolean getSignIn() {
        return signIn;
    }

    public void setSignIn(Boolean signIn) {
        this.signIn = signIn;
    }

    public Integer getSumUser() {
        return sumUser;
    }

    public void setSumUser(Integer sumUser) {
        this.sumUser = sumUser;
    }

    public Integer getSustain() {
        return sustain;
    }

    public void setSustain(Integer sustain) {
        this.sustain = sustain;
    }
}
