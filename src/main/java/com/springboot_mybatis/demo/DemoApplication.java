package com.springboot_mybatis.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //开启定时器扫描test
@MapperScan("com.hohe.mapper")
@ComponentScan(basePackages = {"com.hohe.*"})  //指定扫描路径, 如果没有配置会默认扫描该启动类包及子包下的文件
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}













//@PropertySource(value={"classpath:downLoadFile.properties","classpath:scheduled.properties"})