package com.ihrm.system.utils;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/5/1 22:26
 * 百度ai人脸识别工具类
 */
@Component
public class FaceUtils {
    @Value("${ai.appId}")
    private String APP_ID;
    @Value("${ai.apiKey}")
    private String API_KEY;
    @Value("${ai.secretKey}")
    private String SECRET_KEY;
    @Value("${ai.imageType}")
    private String IMAGE_TYPE;
    @Value("${ai.groupId}")
    private String groupId;

    private AipFace client;
    private HashMap<String, String> options = new HashMap<String, String>(0);

    public FaceUtils() {
        //图片质量  NONE  LOW  NORMAL，HIGH
        options.put("quality_control", "NORMAL");
        //不使用活体检测
        options.put("liveness_control", "NONE");
    }

    @PostConstruct
    public void init(){
        //1.创建java代码和百度云交互的client对象
        client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
    }


    /**
     * 判断用户是否已经注册了面部信息
     * @param userId
     * @return
     */
    public Boolean faceExist(String userId) {
        //返回error_code：0 （存在），非0：不存在
        JSONObject res = client.getUser(userId, groupId, null);
        Integer errorCode = res.getInt("error_code");
        return errorCode == 0;
    }

    /**
     * 人脸注册功能
     *
     * @param bytes  图片的字节码数组
     * @param userId 该用户的id
     * @return Boolean
     */
    public Boolean register(byte[] bytes, String userId) {
        //上传的图片  两种格式 ： url地址，Base64字符串形式
        String encode = Base64Util.encode(bytes);
        //4.调用api方法完成人脸注册
        /**
         * 参数一：（图片的url或者图片的Base64字符串），
         * 参数二：图片形式（URL,BASE64）
         * 参数三：组ID（固定字符串,一旦确定就不能更改）
         * 参数四：用户ID
         * 参数五：hashMap中的基本参数配置
         */
        JSONObject res = client.addUser(encode, "BASE64", groupId, userId, options);
        Integer errorCode = res.getInt("error_code");
        return errorCode == 0;
    }

    /**
     * 人脸更新
     *
     * @param bytes  图片的字节码数组
     * @param userId 该用户id
     * @return Boolean
     */
    public Boolean update(byte[] bytes, String userId) {
        String encode = Base64Util.encode(bytes);
        //4.调用api方法完成人脸注册
        /**
         * 参数一：（图片的url或者图片的Base64字符串），
         * 参数二：图片形式（URL,BASE64）
         * 参数三：组ID（固定字符串）
         * 参数四：用户ID
         * 参数五：hashMap中的基本参数配置
         */
        JSONObject res = client.updateUser(encode, "BASE64", groupId, userId, options);
        Integer errorCode = res.getInt("error_code");
        return errorCode == 0;
    }

    /**
     * 人脸检测:判断图片中是否包含人脸
     *
     * @param bytes 图片的字节码数组
     * @return Boolean
     */
    public Boolean check(byte[] bytes) {
        String image = Base64Util.encode(bytes);

        //调用api方法进行人脸检测
        //参数一：（图片的url或者图片的Base64字符串），
        //参数二：图片形式（URL,BASE64）
        //参数三：hashMap中的基本参数配置（null：使用默认配置）
        JSONObject res = client.detect(image, "BASE64", null);
        if (res.has("error_code") && res.getInt("error_code") == 0) {
            JSONObject resultObject = res.getJSONObject("result");
            Integer faceNum = resultObject.getInt("face_num");
            return faceNum == 1;
        } else {
            return false;
        }
    }

    /**
     *     * 人脸查找：查找人脸库中最相似的人脸并返回数据
     *     *         处理：用户的匹配得分（score）大于80分，即可认为是同一个用户
     *    
     */
    public String faceSearch(byte[] bytes) {
        String image = Base64Util.encode(bytes);
        JSONObject res = client.search(image, IMAGE_TYPE, groupId, options);
        if (res.has("error_code") && res.getInt("error_code") == 0) {
            JSONObject result = res.getJSONObject("result");
            JSONArray userList = result.getJSONArray("user_list");
            if (userList.length() > 0) {
                JSONObject user = userList.getJSONObject(0);
                double score = user.getDouble("score");
                if (score >= 80) {
                    return user.getString("user_id");
                }
            }
        }
        return null;
    }
}
