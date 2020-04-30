package com.ihrm.common.utils;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 22:51
 */
public class ParamCheckUtils {

    /**
     * 判断前端传过来的map类型的参数是否为空
     *
     * @param map map类型的参数
     * @throws CommonException 自定义异常
     */
    public static void checkMapParam(Map map) throws CommonException {
        if (map == null || map.size() <= 0) {
            //这里抛出的异常最终会被全局异常类捕获
            throw new CommonException(ResultCode.PARAMETER_ERROR);
        }
    }
}
