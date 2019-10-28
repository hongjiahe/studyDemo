package com.hohe.service;

import com.hohe.model.User;

import java.util.List;

public interface UserService {

    int addUser(User user);
    List<User> findAllUser(int pageNum, int pageSize);
    User selectByPrimaryKey(Integer id);
    List<User> selectAll();

    int insert(User user);
}
