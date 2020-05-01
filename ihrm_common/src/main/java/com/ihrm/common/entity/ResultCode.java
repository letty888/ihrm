package com.ihrm.common.entity;

/**
 * 公共的返回码
 * 返回码code：
 * 成功：10000
 * 失败：10001
 * 未登录：10002
 * 未授权：10003
 * 抛出异常：99999
 *
 * @author zhang
 */
public enum ResultCode {

    SUCCESS(true, 10000, "操作成功！"),
    //---系统错误返回码-----
    FAIL(false, 10001, "操作失败"),
    UNAUTHENTICATED(false, 10002, "您还未登录"),
    UNAUTHORISE(false, 10003, "权限不足"),
    SERVER_ERROR(false, 99999, "抱歉，系统繁忙，请稍后重试！"),
    PARAMETER_ERROR(false, 77777, "所传参数不符合要求,请重试!"),
    //---用户操作返回码  2xxxx----
    USERNAME_OR_PASSWORD_ERROR(false, 20001, "用户名或密码错误!"),
    LOGIN_TIMEOUT(false, 20002, "抱歉,登录超时,请重新登录!"),
    //---企业操作返回码  3xxxx----

    //---权限操作返回码----
    //---其他操作返回码----
    NO_DATA(false, 00000, "抱歉,没有查询到相应数据"),
    NO_QR_CODE(false, 00001, "此链接已失效,请重新登录"),
    FILE_ERROR(false, 00002, "所传文件为空"),
    NO_INCLUDE_FACE(false, 00003, "图像中未识别到人脸,请重新拍照!");

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    ResultCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
