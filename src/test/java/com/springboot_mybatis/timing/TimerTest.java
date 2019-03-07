package com.springboot_mybatis.timing;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;


@Slf4j
public class TimerTest{

    @Test
    public void test(){

        TimerTask timerTask = new TimerTask(){
            @Override
            public void run(){
                System.out.println("hello timer");
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 2000);

    }

    /*public class TestScheduledExecutorService {
        public static void main(String[] args) {
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            // 参数：1、任务体 2、首次执行的延时时间
            //      3、任务执行间隔 4、间隔时间单位
            service.scheduleAtFixedRate(()->System.out.println("task ScheduledExecutorService "+new Date()), 0, 3, TimeUnit.SECONDS);
        }
    }*/

}
