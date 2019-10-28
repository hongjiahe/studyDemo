package com.hohe.controller;

import com.hohe.model.User;
import com.hohe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/mysql")
public class MysqlReadWriteController {

    public final Logger logger = LoggerFactory.getLogger(MysqlReadWriteController.class);

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/test")
    public void test() {
        try {
            System.out.println("这里走从库");
            List<User> list = userService.selectAll();
            System.out.println(list.size()+"==========");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ResponseBody
    @RequestMapping(value = "/insert")
    public void insert() {
        try {
            System.out.println("这里走主库");

            User user = new User();
            user.setUserId(null);
            user.setPhone("111");
            user.setPassword("222");
            user.setUserName("nidie");
            userService.insert(user);

            System.out.println("结束");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}