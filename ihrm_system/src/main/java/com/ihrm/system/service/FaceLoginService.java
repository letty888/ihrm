package com.ihrm.system.service;

import com.ihrm.common.exception.CommonException;
import com.ihrm.domain.system.response.FaceLoginResult;
import com.ihrm.domain.system.response.QRCode;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/5/1 23:26
 */
public interface FaceLoginService {

    /**
     * 创建二维码
     * @return QRCode
     * @throws Exception
     */
    public QRCode getQRCode() throws Exception;

    /**
     * 根据唯一标识，查询用户是否登录成功
     * @param code
     * @return
     * @throws CommonException
     */
    public FaceLoginResult checkQRCode(String code) throws CommonException;

    public String loginByFace(String code, MultipartFile attachment) throws Exception;


}
