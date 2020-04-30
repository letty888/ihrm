package com.ihrm.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/30 8:26
 * jwt令牌生成和解析工具类
 */
@Data
public class JwtUtils {

    /**
     * 秘钥
     */
    @Value(value = "${jwt.key}")
    private String key;

    /**
     * 有效时间
     */
    @Value(value = "${jwt.ttl}")
    private Long ttl;

    /**
     * 生成jwt
     *
     * @param id   用户id
     * @param name 用户名
     * @param map  其他参数(比如用户其他信息)
     * @return String token字符串
     */
    public String createJwt(String id, String name, Map<String, Object> map) {
        JwtBuilder jwtBuilder =
                Jwts.builder().setId(id).setSubject(name).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, key);
        //设置参数: 内容,有效时间等
        long now = System.currentTimeMillis();
        long expireTime = now + ttl;
        jwtBuilder.setExpiration(new Date(expireTime));
        if (map != null && map.size() > 0) {
            map.forEach(jwtBuilder::claim);
        }
        return jwtBuilder.compact();
    }


    /**
     * 解析token
     *
     * @param token 请求者传递过来的token字符串
     * @return Claims 里面包含了用户基本信息
     */
    public Claims parseJwt(String token) {
        if (!StringUtils.isEmpty(token)) {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        }
        return null;
    }
}
