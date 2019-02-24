package com.hohe.quartz;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * 定时配置（可以配置静态定时任务）
 */
@Configuration
public class SchedulerConfig {


    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();

        jobFactory.setApplicationContext(applicationContext);

        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory, Trigger simpleJobTrigger, Trigger simpleJobTrigger2)
            throws IOException{
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
        factory.setTriggers(simpleJobTrigger, simpleJobTrigger2);

        return factory;
    }

    /**
     * 静态方式配置定时任务
     * @param jobDetail
     * @return
     */
    @Bean
    public CronTriggerFactoryBean simpleJobTrigger(@Qualifier("simpleJobDetail") JobDetail jobDetail) {
        CronTriggerFactoryBean  factoryBean = new CronTriggerFactoryBean ();

        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(1000L);
        factoryBean.setName("trigger1");
        factoryBean.setGroup("group1");
        //周1至周5，每天上午8点至下午18点，每分钟执行一次
        //factoryBean.setCronExpression("0 0/1 8-18 ? * 2-6");
        factoryBean.setCronExpression("0/2 * * * * ?");

        return factoryBean;
    }

    @Bean
    public CronTriggerFactoryBean simpleJobTrigger2(@Qualifier("simpleJobDetail2") JobDetail jobDetail) {
        CronTriggerFactoryBean  factoryBean = new CronTriggerFactoryBean ();

        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(1000L);
        factoryBean.setName("trigger2");
        factoryBean.setGroup("group2");
        //周1至周5，每天上午8点至下午18点，每分钟执行一次
        //factoryBean.setCronExpression("0 0/1 8-18 ? * 2-6");
        factoryBean.setCronExpression("0/3 * * * * ?");

        return factoryBean;
    }

    @Bean
    public JobDetailFactoryBean simpleJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ScheduledJob.class);
        factoryBean.setGroup("group1");
        factoryBean.setName("job1");

        return factoryBean;
    }

    @Bean
    public JobDetailFactoryBean simpleJobDetail2() {
        JobDetailFactoryBean factoryBean2= new JobDetailFactoryBean();
        factoryBean2.setJobClass(ScheduledJob2.class);
        factoryBean2.setGroup("group2");
        factoryBean2.setName("job2");

        return factoryBean2;
    }







    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();

        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));

        propertiesFactoryBean.afterPropertiesSet();

        return propertiesFactoryBean.getObject();
    }

}

