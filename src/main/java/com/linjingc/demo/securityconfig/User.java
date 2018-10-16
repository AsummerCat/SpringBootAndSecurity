package com.linjingc.demo.securityconfig;


import lombok.Data;
import lombok.ToString;

import java.util.Set;

/**
 * 用户表
 * 通过扩展UserDetails接口扩展自己的用户信息
 */
@Data
@ToString
public class User {
    private Long id;
    private String username;
    private String password;
    private Set roles;


}
