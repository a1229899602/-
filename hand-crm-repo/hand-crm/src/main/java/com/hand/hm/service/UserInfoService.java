package com.hand.hm.service;

import com.hand.frame.model.DataOutput;
import com.hand.hm.access.bean.UserInfo;
import com.hand.hm.access.vo.SignInVo;
import com.hand.hm.access.vo.UserInfoVo;

import java.util.Map;

public interface UserInfoService {

    /**
     * 新增
     */
    DataOutput<String> insert(UserInfo userInfo);

    /**
     * 删除
     */
    DataOutput<String> delete(Integer id);


    DataOutput<UserInfoVo> getUserInfo(String id);

    /**
     * 更新
     */
    DataOutput<String> update(UserInfo userInfo);

    /**
     * 根據主鍵 id 查詢
     */
    DataOutput<UserInfo> load(Integer id);

    /**
     * 分页查询
     */
    Map<String, Object> pageList(Integer offset, Integer pagesize);


    DataOutput<SignInVo> signIn(String openId);
}