package com.linjingc.demo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cxc
 * @date 2018/10/16 15:44
 * 登陆控制器
 */
@Controller
public class LoginConrtoller {

    @GetMapping(value = "/")
    public String index() {
        return "hello";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("heihei")
    public String heihei() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();   //登陆后的账号 转换为 UserDetails
        return "heihei";
    }

    /**
     * 退出后的页面
     */
    @RequestMapping("home")
    @ResponseBody
    public Object home() {
        Map<String, String> map = new HashMap<>(1);
        map.put("message", "退出成功");
        return map;
    }

    /**
     * 拥有admin这个角色
     */
    @PreAuthorize("hasRole('admin')")
    @RequestMapping("admin")
    @ResponseBody
    public Object admin() {
        Map<String, String> map = new HashMap<>(1);
        map.put("message", "你拥有admin这个权限");
        return map;
    }

    /**
     * 拥有ROLE_AAA这个角色
     */
    @PreAuthorize("hasRole('ROLE_AAA')")
    @RequestMapping("add")
    @ResponseBody
    public Object add() {
        Map<String, String> map = new HashMap<>(1);
        map.put("message", "你拥有ROLE_AAA这个权限");
        return map;
    }

    /**
     * 拥有update这个权限
     */
    @PreAuthorize("hasAuthority('update')")
    @RequestMapping("update")
    @ResponseBody
    public Object update() {
        Map<String, String> map = new HashMap<>(1);
        map.put("message", "你拥有update这个权限");
        return map;
    }

    /**
     * 拥有update这个权限
     */
    @PreAuthorize("hasPermission('update')")
    @RequestMapping("update1")
    @ResponseBody
    public Object update1() {
        Map<String, String> map = new HashMap<>(1);
        map.put("message", "你拥有update这个权限");
        return map;
    }

    /**
     * 记住我了
     */
    @PreAuthorize("isRememberMe()")
    @RequestMapping("me")
    @ResponseBody
    public Object isRememberMe() {
        Map<String, String> map = new HashMap<>(1);
        map.put("message", "记住我了");
        return map;
    }

    //--------

    @Secured("ROLE_AAA")
    @RequestMapping("AAA")
    @ResponseBody
    public Object AAA() {
        Map<String, String> map = new HashMap<>(1);
        map.put("message", "你拥有ROLE_AAA这个权限");
        return map;
    }
}
