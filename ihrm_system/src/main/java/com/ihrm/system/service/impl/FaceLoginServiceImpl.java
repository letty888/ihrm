package com.ihrm.system.service.impl;

import com.baidu.aip.util.Base64Util;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.constants.RedisConstsnts;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.FaceLoginResult;
import com.ihrm.domain.system.response.QRCode;
import com.ihrm.system.dao.UserDao;
import com.ihrm.system.service.FaceLoginService;
import com.ihrm.system.utils.FaceUtils;
import com.ihrm.system.utils.QRCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/5/1 23:26
 */
@Service
public class FaceLoginServiceImpl implements FaceLoginService {

    @Value("${qr.url}")
    private String url;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private QRCodeUtil qrCodeUtil;

    @Autowired
    private FaceUtils faceUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    //创建二维码
    @Override
    public QRCode getQRCode() throws Exception {
        String code = idWorker.nextId() + "";
        String content = url + "?code=" + code;
        System.out.println(content);
        String qrCode = qrCodeUtil.crateQRCode(content);
        //二维码默认状态为未扫描
        FaceLoginResult faceLoginResult = new FaceLoginResult("-1");
        //将二维码信息存入到redis中
        redisTemplate.boundValueOps(RedisConstsnts.QR_CODE_NAME + code).set(faceLoginResult, 10, TimeUnit.MINUTES);
        return new QRCode(code, qrCode);
    }

    /**
     * 根据唯一标识，查询用户是否登录成功
     *
     * @param code
     * @return
     * @throws CommonException
     */
    @Override
    public FaceLoginResult checkQRCode(String code) throws CommonException {
        FaceLoginResult faceLoginResult = (FaceLoginResult) redisTemplate.boundValueOps(RedisConstsnts.QR_CODE_NAME + code).get();
        return faceLoginResult;
    }


    @Autowired
    private UserDao userDao;

    //扫描二维码之后，使用拍摄照片进行登录
    //返回值：登录成功之后返回的用户id
    //登录失败：null
    @Override
    public String loginByFace(String code, MultipartFile attachment) throws Exception {
        //调用百度云ai进行人脸比对
        String userId = faceUtils.faceSearch(attachment.getBytes());
        FaceLoginResult faceLoginResult = new FaceLoginResult("0");
        if (!StringUtils.isEmpty(userId)) {
            Optional<User> optionalUser = userDao.findById(userId);
            if (!optionalUser.isPresent()) {
                return null;
            }
            User user = optionalUser.get();
            //说明此用户身份合法,可以正常登录
            Subject subject = SecurityUtils.getSubject();
            subject.login(new UsernamePasswordToken(user.getMobile(), user.getPassword()));
            String token = subject.getSession().getId().toString();
            faceLoginResult = new FaceLoginResult("1", token, user.getId());
        }
        redisTemplate.boundValueOps(RedisConstsnts.QR_CODE_NAME + code).set(faceLoginResult);
        return userId;
    }


}
