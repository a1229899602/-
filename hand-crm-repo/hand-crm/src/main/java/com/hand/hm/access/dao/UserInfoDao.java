package com.hand.hm.access.dao;

import com.hand.hm.access.bean.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserInfoDao {

    int insert(UserInfo userInfo);

    int delete(int id);

    int update(UserInfo userInfo);

    UserInfo load(int id);

    List<UserInfo> pageList(int offset, int pagesize);

    int pageListCount(int offset, int pagesize);

    UserInfo getUserInfoByOpenId(String openId);

}
