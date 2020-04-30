package com.ihrm.company;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;


/**
 * @author zhang
 */
@SpringBootApplication(scanBasePackages = "com.ihrm")
@EntityScan(basePackages={"com.ihrm.domain.company"})
public class CompanyApplication {

    /**
     * 启动方法
     */
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }

    //解决jwt+jpa多对多查询出现no session的问题
    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }
}
