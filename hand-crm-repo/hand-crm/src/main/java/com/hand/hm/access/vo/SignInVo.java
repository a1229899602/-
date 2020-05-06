package com.hand.hm.access.vo;

import io.swagger.annotations.ApiModelProperty;

public class SignInVo {

    /**
     * 主键
     */
    @ApiModelProperty("红包金额")
    private Double bonusAmount;

    @ApiModelProperty("是否有礼品  true  false")
    private Boolean award;

    @ApiModelProperty("礼品Id")
    private String presentId;

    @ApiModelProperty("礼品名称")
    private String presentName;

    /**
     * 已签到的天数
     */
    @ApiModelProperty("已签到的天数")
    private String signedInDays;

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Boolean getAward() {
        return award;
    }

    public void setAward(Boolean award) {
        this.award = award;
    }

    public String getPresentId() {
        return presentId;
    }

    public void setPresentId(String presentId) {
        this.presentId = presentId;
    }

    public String getPresentName() {
        return presentName;
    }

    public void setPresentName(String presentName) {
        this.presentName = presentName;
    }

    public String getSignedInDays() {
        return signedInDays;
    }

    public void setSignedInDays(String signedInDays) {
        this.signedInDays = signedInDays;
    }
}
