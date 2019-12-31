package com.shu.smallvideo;

import io.swagger.annotations.Api;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author shuxibing
 * @date 2019/12/22 15:33
 * @uint d9lab
 * @Description:
 */
@SpringBootApplication
@MapperScan(basePackages = "com.shu.smallvideo.mapper")
@EnableScheduling //定时任务
public class App {
    public static void main(String[] args){
        SpringApplication.run(App.class,args);
    }


}
