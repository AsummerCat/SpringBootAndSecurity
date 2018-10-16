package com.linjingc.demo.service;

import com.linjingc.demo.securityconfig.User;
import com.linjingc.demo.util.BCryptEncoderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义查询用户    Security接口
 */
@Slf4j
public class CustomUserService implements UserDetailsService { //实现 这个用户验证接口
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {   //这里重构了原来的的方法
        //User user = userRepository.findByUsername(s); //jpa 查询数据库
        User user = new User();
        user.setUsername(userName);    //模拟查询
        user.setPassword(BCryptEncoderUtil.encoder("123456"));


        List<SimpleGrantedAuthority> auths = new ArrayList<>();  //权限
        auths.add(new SimpleGrantedAuthority("add"));//这里添加权限 可以添加多个权限
        auths.add(new SimpleGrantedAuthority("update"));
        auths.add(new SimpleGrantedAuthority("delete"));
        auths.add(new SimpleGrantedAuthority("USER"));
        auths.add(new SimpleGrantedAuthority("ROLE_AAA"));  //任何以ROLE_开头的权限都被视为角色


        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        log.info("username:" + user.getUsername() + ";password:" + user.getPassword());
        org.springframework.security.core.userdetails.User a = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), auths);
        a.getAuthorities().forEach(data -> log.info(a.getUsername() + "的权限:" + data));
        return a;
    }
}