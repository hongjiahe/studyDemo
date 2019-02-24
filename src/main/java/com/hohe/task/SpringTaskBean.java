package com.hohe.task;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Setter
@Getter
@PropertySource("classpath:spring_task.properties") //加载配置文件
@Configuration //表示该类为配置类
@EnableAsync  //开启异步事件支持
public class SpringTaskBean {

    /**
     * 核心线程数
     */
    @Value("${corePoolSize}")
    private Integer corePoolSize;

    /**
     * 最大线程数
     */
    @Value("${maxPoolSize}")
    private Integer maxPoolSize;

    /**
     * 队列大小
     */
    @Value("${queueCapacity}")
    private Integer queueCapacity;

    //keepAliveSeconds 线程最大空闲时间

    @Bean
    public ThreadPoolTaskExecutor task(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.corePoolSize);
        executor.setMaxPoolSize(this.maxPoolSize);
        executor.setQueueCapacity(this.queueCapacity);
        executor.initialize();
        return executor;
    }

}
