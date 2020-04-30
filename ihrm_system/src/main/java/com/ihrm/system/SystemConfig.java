package com.ihrm.system;

import com.ihrm.common.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author zhang
 * jwt令牌拦截配置类
 */
@Configuration
public class SystemConfig extends WebMvcConfigurationSupport {

    private final JwtInterceptor jwtInterceptor;

    public SystemConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    /**
     * 添加拦截器的配置
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //1.添加自定义拦截器
        registry.addInterceptor(jwtInterceptor).
                //2.指定拦截器的url地址
                        addPathPatterns("/**").
                //3.指定不拦截的url地址
                        excludePathPatterns("/sys/login","/frame/register/**");
    }
}
