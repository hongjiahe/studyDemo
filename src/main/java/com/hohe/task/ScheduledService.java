package com.hohe.task;


import com.hohe.task.SpringTaskBean;
import com.hohe.model.User;
import com.hohe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.util.Date;

@Slf4j   //日志注解 (lombok)
@Component
public class ScheduledService {

    @Autowired  private UserService userService;

    @Autowired private SpringTaskBean springTaskBean;

    /**
     * Spring Task
     */
   //@Scheduled(cron = "0/2 * * * * *")  // cron表达式, 这里的意思是每两秒钟执行一次 (更多表达式可搜索: 在线Cron表达式生成器)
    public void testAdd() {

        ThreadPoolTaskExecutor task = springTaskBean.task();
        task.execute(new Runnable() {
            @Override
            public void run() {

                Thread thread = Thread.currentThread();
                log.info("========= before--->吴彦祖 ============="+thread.getName()+"===============>"+ new Date());

                try {
                    /*log.info(thread.getName()+"=============threadName");
                    System.out.println(thread.getId()+"=============threadId");*/
                    thread.sleep(4000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                User user = new User();
                user.setUserName("普宁吴彦祖");
                user.setPassword("a123456");
                user.setPhone("130****2328");
                userService.addUser(user);
                log.info("定时添加用户_吴彦祖========"+thread.getName()+"===============>" + new Date());

             }
        });


       /* log.info("========= before--->吴彦祖 ============="+ new Date());


        Thread.sleep(4000); //线程休眠4秒钟

        User user = new User();
        user.setUserName("普宁吴彦祖");
        user.setPassword("a123456");
        user.setPhone("130****2328");
        userService.addUser(user);

        log.info("定时添加用户_吴彦祖" + new Date());*/
    }



    //@Scheduled(cron = "0/2 * * * * *")  // cron表达式, 这里的意思是每两秒钟执行一次 (更多表达式可搜索: 在线Cron表达式生成器)
    public void testAdd22() {

        ThreadPoolTaskExecutor task = springTaskBean.task();
        task.execute(new Runnable() {
            @Override
            public void run() {

                Thread thread = Thread.currentThread();
                log.info("========= before--->洪家河 =============" + thread.getName() + "===============>" + new Date());

                try {
                    /*log.info(thread.getName()+"=============threadName");
                    System.out.println(thread.getId()+"=============threadId");*/
                    thread.sleep(4000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                User user = new User();
                user.setUserName("普宁洪家河");
                user.setPassword("a123456");
                user.setPhone("130****2328");
                userService.addUser(user);
                log.info("定时添加用户_洪家河========" + thread.getName() + "===============>" + new Date());

            }
        });

    }

    //@Scheduled(cron = "0/2 * * * * *")  // cron表达式, 这里的意思是每两秒钟执行一次 (更多表达式可搜索: 在线Cron表达式生成器)
    public void testAdd2()  {

        Thread thread = Thread.currentThread();

        log.info("========= before--->彭于晏 ============="+thread.getName()+"=========================="+ new Date());

        User user = new User();
        user.setUserName("普宁彭于晏");
        user.setPassword("a123456");
        user.setPhone("130****2328");
        userService.addUser(user);

        log.info("定时添加用户_彭于晏=========================" +thread.getName()+"=========================="+ new Date());
    }


   // @Scheduled(cron = "0/2 * * * * *")  // cron表达式, 这里的意思是每两秒钟执行一次 (更多表达式可搜索: 在线Cron表达式生成器)
    public void testAdd3()  {

        Thread thread = Thread.currentThread();

        log.info("========= before--->洪家河 ============="+thread.getName()+"=========================="+ new Date());

        User user = new User();
        user.setUserName("普宁洪家河");
        user.setPassword("a123456");
        user.setPhone("130****2328");
        userService.addUser(user);

        log.info("定时添加用户_洪家河=========================" +thread.getName()+"=========================="+ new Date());
    }



}
