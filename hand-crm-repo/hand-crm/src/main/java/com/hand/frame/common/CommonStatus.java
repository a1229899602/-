package com.hand.frame.common;

import com.hand.frame.model.OutputStatus;

import java.util.Arrays;

public class CommonStatus {
    public static final OutputStatus SUCCESS = new OutputStatus(0, "请求已成功");
    public static final OutputStatus BAD_REQUEST = new OutputStatus(9902, "请求参数有误");
    public static final OutputStatus UNAUTHORIZED = new OutputStatus(9903, "当前请求需要用户登录");
    public static final OutputStatus FORBIDDEN = new OutputStatus(9904, "没有访问权限");
    public static final OutputStatus NOT_FOUND = new OutputStatus(9905, "找不到请求的资源");
    public static final OutputStatus SERVER_ERROR = new OutputStatus(9906, "服务器处理过程中出现未处理异常");

    public CommonStatus() {
    }
}
