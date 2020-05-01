package com.ihrm.common.exception;

import com.ihrm.common.entity.ResultCode;
import lombok.Getter;

/**
 * 自定义异常
 *
 * @author zhang
 */
@Getter
public class CommonException extends Exception {

    private static final long serialVersionUID = -1306917846740039344L;

    private ResultCode resultCode;

    public CommonException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
