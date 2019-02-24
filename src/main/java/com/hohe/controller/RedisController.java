package com.hohe.controller;

import com.hohe.log.annotation.SysOperationLog;
import com.hohe.model.User;
import com.hohe.service.UserService;
import com.hohe.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/redis")
public class RedisController {


    public final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    public String key = "666";


    @ResponseBody
    @RequestMapping(value = "/test")
    @SysOperationLog(functionName = "test哈哈哈")
    public void test(){

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
            User user2 = userService.selectByPrimaryKey(3);
            logger.info(user.getUserName());

            //写入缓存
            //redisUtils.set(key.concat(user.getUserId().toString()),user);
            logger.info(user.getUserId()+"------------> id");

            //redisUtils.set(user.getUserId()+"",user);
            redisUtils.set(user.getUserId()+"", user, 10L, TimeUnit.DAYS);
            redisUtils.set(user2.getUserId()+"", user, 10L, TimeUnit.DAYS);

            logger.info("数据插入缓存"+user.toString());
        }


    }


    @ResponseBody
    @RequestMapping(value = "/testRedis")
    public void testRedis(String key){

        if(redisUtils.exists(key)){
            logger.info("存在该key : "+ key);
            User user = (User) redisUtils.get(key);
            logger.info(user.getUserName());
            logger.info(user.getPassword());
        }else{
            logger.info("不存在");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/testRemove")
    public void testRemove(String key){
        //redisUtils.remove(key);
        redisUtils.removePattern(key);

    }

    @ResponseBody
    @RequestMapping(value = "/testSet")
    public void testRemove(){
        redisUtils.hmSet("hohe", "hongjiahe", "479021229");
    }


    @ResponseBody
    @RequestMapping(value = "/getKey")
    public void testGetKey(String key, Object hashKey){

        Object object = redisUtils.hmGet(key+"", hashKey+"");
        System.out.println(object);

    }

    @ResponseBody
    @RequestMapping(value = "/testIncr")
    public void testIncr(){
        Long incr = redisUtils.incr();
        System.out.println(incr);
    }

    @ResponseBody
    @RequestMapping(value = "/getValueByKey")
    public void getValueByKey(String key){
        Object o = redisUtils.get(key);
        System.out.println(o);
    }



}