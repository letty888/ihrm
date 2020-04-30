package com.ihrm.common.interceptor;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/30 17:29
 * 全局拦截器(为了用户成功登录之后将用户的信息共享到request域中)
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private  JwtUtils jwtUtils;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization) || !authorization.startsWith("Bearer")) {
            throw new CommonException(ResultCode.UNAUTHENTICATED);
        }
        String token = authorization.replace("Bearer ", "");
        if (StringUtils.isEmpty(token)) {
            throw new CommonException(ResultCode.UNAUTHENTICATED);
        }
        try {
            Claims claims = jwtUtils.parseJwt(token);
            if (StringUtils.isEmpty(claims)) {
                throw new CommonException(ResultCode.UNAUTHENTICATED);
            }
            //将claims共享到request域中
            request.setAttribute("user_claims", claims);
            return true;
            //利用反射判断该用户具有的api相关权限是否可以执行想要执行的方法(注意有坑)
          /*  String permissonApis = (String) claims.get("permissonApis");
            //获取目标方法
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequestMapping methodAnnotation = handlerMethod.getMethodAnnotation(RequestMapping.class);
            //获取到RequestMapping注解中的name属性
            assert methodAnnotation != null;
            String name = methodAnnotation.name();
            if (!StringUtils.isEmpty(permissonApis)) {
                if(StringUtils.isEmpty(name)){
                    //如果RequestMapping注解中没有name属性,则说明此接口可以直接访问
                    //将claims共享到request域中
                    request.setAttribute("user_claims", claims);
                    return true;
                }
                if (permissonApis.contains(name)) {
                    //将claims共享到request域中
                    request.setAttribute("user_claims", claims);
                    return true;
                } else {
                    throw new CommonException(ResultCode.UNAUTHORISE);
                }
            } else {
                throw new CommonException(ResultCode.SERVER_ERROR);
            }
*/
        } catch (Exception e) {
            throw new CommonException(ResultCode.LOGIN_TIMEOUT);
        }
    }
}
