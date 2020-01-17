package com.hohe.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Setter
@Getter
@ToString
public class User {

    private Integer userId;
    private String userName;
    private String password;
    private String phone;
    private String createName;

}