package com.hand.hm.service;

import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.hand.frame.config.WxProperties;
import com.hand.frame.model.DataOutput;
import com.hand.frame.util.BaseUtils;
import com.hand.hm.access.bean.UserInfo;
import com.hand.hm.access.dao.UserInfoDao;
import com.hand.hm.access.vo.SignInVo;
import com.hand.hm.access.vo.UserInfoVo;
import com.hand.wechat.util.IpUtil;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoMapper;

    @Autowired
    private BaseUtils baseUtils;

    @Override
    public DataOutput insert(UserInfo userInfo) {

        if (userInfo == null) {
            return baseUtils.getErrorMsg("参数缺失");
        }
        userInfoMapper.insert(userInfo);
        return baseUtils.getSuccessMsg();
    }


    @Override
    public DataOutput delete(Integer id) {
        int ret = userInfoMapper.delete(id);
        return baseUtils.getSuccessMsg();
    }

    @Override
    public DataOutput<UserInfoVo> getUserInfo(String openId) {
        if (openId == null) {
            return baseUtils.getErrorMsg();
        }
        UserInfo userInfo = userInfoMapper.getUserInfoByOpenId(openId);

        if (userInfo == null) {
            userInfo = new UserInfo();
           // userInfo.setId();
          //  userInfo.setPhone();
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        if (openId.equals(1)) {
            userInfoVo.setId("10010");
            userInfoVo.setUserName("马化腾");
            userInfoVo.setPhone("10000");
            userInfoVo.setSignedInDays("6");
            userInfoVo.setSignIn(false);
            userInfoVo.setSumUser(10000);
            userInfoVo.setSustain(5);
            userInfoVo.setUserId("1");
        } else {
            userInfoVo.setUserId("2");
            userInfoVo.setId("10086");
            userInfoVo.setUserName("马云");
            userInfoVo.setPhone("4552");
            userInfoVo.setSignedInDays("10");
            userInfoVo.setSignIn(true);
            userInfoVo.setSumUser(10000);
            userInfoVo.setSustain(5);
        }
        return baseUtils.getSuccessMsg(userInfoVo);
    }


    @Override
    public DataOutput<String> update(UserInfo userInfo) {
        int ret = userInfoMapper.update(userInfo);
        return baseUtils.getSuccessMsg();
    }

    @Autowired
    private WxProperties wxProperties;

    @Autowired
    private WxPayService wxPayService;

    @Override
    public DataOutput<UserInfo> load(Integer id) {
        return baseUtils.getSuccessMsg(userInfoMapper.load(id));
    }

    @Override
    public Map<String, Object> pageList(Integer offset, Integer pagesize) {
        List<UserInfo> pageList = userInfoMapper.pageList(offset, pagesize);
        int totalCount = userInfoMapper.pageListCount(offset, pagesize);
        // result
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public DataOutput<SignInVo> signIn(String openId) {
      /*  if (openId == null) {
            return baseUtils.getErrorMsg();
        }
        SignInVo signInVo = new SignInVo();
        if (openId.equals(1)) {
            signInVo.setAward(false);
            signInVo.setBonusAmount(0.5);
            signInVo.setPresentId("");
            signInVo.setPresentName("");
            signInVo.setSignedInDays("6");
        } else {
            signInVo.setAward(true);
            signInVo.setBonusAmount(1.00);
            signInVo.setPresentId("p8I5gw_PNIBxXHkABXhth9kXVtHA");
            signInVo.setPresentName("经典咖啡杯");
            signInVo.setSignedInDays("10");
        }*/

        EntPayRequest entPayRequest = new EntPayRequest();
        entPayRequest.setAppid(wxProperties.getAppId());
        entPayRequest.setMchId(wxProperties.getMchId());
        entPayRequest.setPartnerTradeNo("12345678998765412365474125865");
        entPayRequest.setOpenid(openId);
        entPayRequest.setCheckName("NO_CHECK");
        entPayRequest.setAmount(100);
        entPayRequest.setDescription("还钱");
        entPayRequest.setSpbillCreateIp("192.168.0.1");
        EntPayResult entPayResult = null;
        try {
            entPayResult = wxPayService.getEntPayService().entPay(entPayRequest);
            System.out.println(entPayResult);
        } catch (WxPayException e) {
            return baseUtils.getErrorMsg("付款失败，返回报文" + e);
        }
        return baseUtils.getSuccessMsg();

    }
}