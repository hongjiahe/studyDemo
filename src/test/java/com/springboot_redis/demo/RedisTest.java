package com.springboot_redis.demo;

import com.hohe.model.User;
import com.hohe.service.UserService;
import com.hohe.service.impl.UserServiceImpl;
import com.hohe.util.RedisUtils;
import com.sun.glass.ui.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTest {

    private static final String key = "springboot1219";//这里的key值可以自己修改
    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;


    /**
     *查询数据
     *并将数据数据存在redis里
     */
    @Test
    public void testfind() {
        //如果缓存存在
        boolean hasKey = redisUtils.exists(key.concat("1011"));
        if(hasKey){
            //获取缓存
            Object object =  redisUtils.get(key.concat("1011"));
            logger.info("从缓存获取的数据"+ object);
        }else{
            //从DB中获取信息
            logger.info("从数据库中获取数据");

            User user = userService.selectByPrimaryKey(2);
            logger.info(user.getUserName());

            //写入缓存
            redisUtils.set(key.concat(user.getUserId().toString()),user);
            logger.info("数据插入缓存"+user.toString());
        }

    }

    /**
     * @Date:10:11 2017/12/20
     *删除数据
     *
     */
    @Test
    public void testdel(){
        //缓存存在
        boolean hasKey = redisUtils.exists(key.concat("1011"));
        if(hasKey){
            Object object =  redisUtils.get(key.concat("1011"));
            redisUtils.remove(key.concat("1011"));
            logger.info("从缓存中删除数据");
        }else {
            logger.info("缓存中没有数据！");
        }
    }

}
