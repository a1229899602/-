package com.hand.hm.controller;


import com.hand.frame.model.DataOutput;
import com.hand.hm.access.vo.SignInVo;
import com.hand.hm.access.vo.UserInfoVo;
import com.hand.hm.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(value = "user", description = "签到相关接口", tags = {"user"})
@RequestMapping(value = "/user")
public class SignInController {


    @Resource
    private UserInfoService userInfoService;

    //用户信息
    @ApiOperation("用户信息 包括签到信息 是否可以签到等")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public DataOutput<UserInfoVo> getUserInfo(@ApiParam(value = "openId", required = true) @RequestParam(value = "openId") String openId) {
        return userInfoService.getUserInfo(openId);
    }

    //打卡
    @ApiOperation("打卡按钮")
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public DataOutput<SignInVo> signIn(@ApiParam(value = "openId", required = true) @RequestParam(value = "openId") String openId) {
        return userInfoService.signIn(openId);
    }


}
