package com.hand.frame.util;

import com.hand.frame.common.CommonStatus;
import com.hand.frame.model.DataOutput;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Function:
 * Date: 2018/11/20
 *
 * @author skipper
 * @desc 封装一些通用的函数
 * @see
 */
@Service
public class BaseUtils {


    private static volatile BaseUtils baseUtils;

    private BaseUtils() {
    }

    /**
     * 线程安全获取util单例
     *
     * @return
     */
    public static BaseUtils getInstance() {
        if (baseUtils == null) {
            synchronized (BaseUtils.class) {
                if (baseUtils == null) {
                    baseUtils = new BaseUtils();
                }
            }
        }
        return baseUtils;
    }


    /**
     * 将实体类转为DTO类型
     */
    public <S, T> T convertEntityToDto(S source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 判断dto是否为空
     *
     * @param dto
     * @return
     */
    public boolean dtoIsnull(Object dto) {
        return null == dto;
    }

    /**
     * 获取通用的成功信息返回
     *
     * @return
     */
    public DataOutput getSuccessMsg() {
        return new DataOutput<>(CommonStatus.SUCCESS);
    }

    /**
     * 获取通用的成功信息返回
     *
     * @param dataOutput
     * @return
     */
    public DataOutput getSuccessMsg(Object dataOutput) {
        return new DataOutput<>(dataOutput);
    }


    /**
     * 获取通用的成功信息返回
     *
     * @return
     */
    public DataOutput getErrorMsg() {
        return new DataOutput(CommonStatus.SERVER_ERROR);
    }

    /**
     * 获取通用的成功信息返回
     *
     * @param msg
     * @return
     */
    public DataOutput getErrorMsg(String msg) {
        return new DataOutput(CommonStatus.SERVER_ERROR.getCode(), msg
        );
    }

    /**
     * 获取通用的
     *
     * @return
     */
    public DataOutput getBadErrorMsg() {
        return new DataOutput(CommonStatus.BAD_REQUEST);
    }

    /**
     * 获取通用的参数错误信息
     *
     * @param msg
     * @return
     */
    public DataOutput getBadErrorMsg(String msg) {
        return new DataOutput(CommonStatus.BAD_REQUEST.getCode(), msg
        );
    }

    /**
     * 获取通用的错误信息 返回自定义错误消息提醒
     *
     * @param msg 自定义错误
     * @return
     */
    public DataOutput getMyErrorMsg(String msg) {
        return new DataOutput(CommonStatus.SERVER_ERROR.getCode(), msg);
    }




    /**
     * 判断多个参数 都不为空  如有一个为空 则返回true
     *
     * @param cs
     * @return true false
     */
    public boolean parametersIsBlank(CharSequence... cs) {
        for (CharSequence charSequence : cs) {
            if (isBlank(charSequence)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断多个参数 都不为空  如有一个为空 则返回true
     *
     * @param objects
     * @return true false
     */
    public boolean parametersIsBlank(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用session中根据key 获取
     *
     * @param key
     * @param request
     * @return
     */
    public Object getFromSession(String key, HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute(key);
    }


    public boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public Date getNow() {
        return new Date();
    }

    /**
     * 获取Ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAdrress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if (index != -1) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }
        XFor = Xip;
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

}
