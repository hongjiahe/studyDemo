package com.hohe.quartz;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Date;

/**
 * 自定义定时任务
 */
public class ScheduledJob2 implements Job {

    private static final Logger logger= Logger.getLogger(ScheduledJob2.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        //执行任务逻辑....ScheduledJob2
        //logger.info("执行自定义定时任务==== ScheduledJob2 =====>"+ new Date()+"==============>"+jobKey.getName());
    }
}