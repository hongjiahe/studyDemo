package com.hohe.test;


import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CountDownLatch;

/**
 *
 *  CountDownLatch  的使用
 *
 *   CountDownLatch使用场景及分析
 * 　　JDk1.5提供了一个非常有用的包，Concurrent包，这个包主要用来操作一些并发操作，提供一些并发类，可以方便在项目当中傻瓜式应用。
 * 　　JDK1.5以前，使用并发操作，都是通过Thread，Runnable来操作多线程；但是在JDK1.5之后，提供了非常方便的线程池（ThreadExecutorPool），主要代码由大牛Doug Lea完成，其实是在jdk1.4时代，由于java语言内置对多线程编程的支持比较基础和有限，所以他写了这个，因为实在太过于优秀，所以被加入到jdk之中；
 * 　　
 *     这次主要对CountDownLatch进行系统的讲解
 * 　　使用场景：比如对于马拉松比赛，进行排名计算，参赛者的排名，肯定是跑完比赛之后，进行计算得出的，翻译成Java识别的预发，
 *              就是N个线程执行操作，主线程等到N个子线程执行完毕之后，在继续往下执行。
 *
 *     主线程负责: 鸣抢示意比赛开始
 *     子线程负责: 选手的比赛 (过程)
 *     主线程负责: 计算选手比赛排名
 *
 */

@Controller
@RequestMapping(value = "/countDownLatchTest")
public class CountDownLatchTest {


    @ResponseBody
    @RequestMapping(value = "/test")
    public void testRedis(){

        try {
            System.out.println("比赛开始");
            final CountDownLatch latch = new CountDownLatch(3);
            for(int i = 0 ; i< 3; i++){

                Thread.sleep(500);

                new Thread(new MyWork(latch)).start();
            }
            latch.await();
            System.out.println("比赛结束(计算排名...)");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected static class MyWork implements Runnable{
        final CountDownLatch latch;

        public MyWork(CountDownLatch latch){
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("runing==========>"+ latch);
            latch.countDown();
        }
    }

}















