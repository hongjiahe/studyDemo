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

@Controller
@RequestMapping(value = "/user")
public class UserController {


    public final Logger logger = LoggerFactory.getLogger(UserController.class);

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
            User user = userService.selectByPrimaryKey(3);
            logger.info(user.getUserName());

            //写入缓存
            //redisUtils.set(key.concat(user.getUserId().toString()),user);
            logger.info(user.getUserId()+"------------> id");

            redisUtils.set(user.getUserId()+"",user);
            redisUtils.set("2",user);
            logger.info("数据插入缓存"+user.toString());
        }


    }

    @ResponseBody
    @RequestMapping(value = "/add")
    public Integer add(User user){
        return userService.addUser(user);
    }

    @ResponseBody
    @RequestMapping(value = "/testRedis")
    public void testRedis(String key){

        logger.info("info+'123123123q212312洪家河是帅哥");

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
        redisUtils.remove(key);
    }

}