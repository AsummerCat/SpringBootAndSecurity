package com.linjingc.demo.securityconfig;

import com.linjingc.demo.service.CustomUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * @author cxc
 * @date 2018/10/16 15:36
 * Security配置类
 */
@Slf4j
@Configuration
@EnableWebSecurity //注解开启Security
@EnableGlobalMethodSecurity(prePostEnabled = true,jsr250Enabled=true)   //开启Security注解  然后在controller中就可以使用方法注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    UserDetailsService customUserService() {
        return new CustomUserService();
    }

    /**
     * 这里可以设置忽略的路径或者文件
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略css.jq.img等文件
        log.info("--------------------------SecurityConfig忽略文件及路径----------------------------");
        web.ignoring().antMatchers("/**.html", "/**.css", "/img/**", "/**.js", "/third-party/**");
    }


    /**
     * 这里是权限控制配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("--------------------------SecurityConfig加载成功----------------------------");
        http.authorizeRequests()
                .antMatchers("/home", "/**.html", "/**.html", "/**.css", "/img/**", "/**.js", "/third-party/**").permitAll() //不需要权限访问
                .antMatchers("/", "/index/", "/index/**").authenticated()   //该路径需要验证通过
                .antMatchers("/lo").access("hasRole('AAA') or hasAuthority('add')") //该路径需要角色  or 权限XXX
                //还有一种方法 就是在方法名称上写注解    @PreAuthorize("hasAnyAuthority('USER','delete')")   //注解拦截权限
                //任何以ROLE_开头的权限都被视为角色
                .anyRequest().authenticated() //都要权限  放在最后
                .and()
                //开启cookie保存用户数据
                .rememberMe()
                //设置cookie有效期
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                .and()
                .formLogin()
                .loginPage("/login")   //自定义登录页
                .usernameParameter("username")//自定义用户名参数名称
                .passwordParameter("password")//自定义密码参数名称
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/home")//退出登录后的默认url是"/home"
                .permitAll();
    }


    /**
     * 这里是验证登录并且赋予权限
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("--------------------------Security自定义验证登录赋予权限方法加载成功----------------------------");

        /**
         * 方式一 写在内存中的角色
         */
        auth.inMemoryAuthentication().passwordEncoder(bCryptPasswordEncoder())//在此处应用自定义PasswordEncoder
                .withUser("user").password("password").roles("USER")  //写在内存中的角色
                .and() //这个是指可以写多个
                .withUser("admin").password("password").authorities("ROLE_USER", "ROLE_ADMIN");

        /**
         * 方式二 数据库查询用户信息
         */
        auth.userDetailsService(customUserService()).passwordEncoder(bCryptPasswordEncoder());//添加自定义的userDetailsService认证  //现在已经要加密.passwordEncoder(new MyPasswordEncoder())
        auth.eraseCredentials(false);   //这里是清除还是不清除登录的密码  SecurityContextHolder中
    }


    /**
     * 不加密 官方已经不推荐了
     * 自定义密码加密器
     */
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    /**
     * BCryptPasswordEncoder 使用BCrypt的强散列哈希加密实现，并可以由客户端指定加密的强度strength，强度越高安全性自然就越高，默认为10.
     * 自定义密码加密器
     * BCryptPasswordEncoder(int strength, SecureRandom random)
     * SecureRandom secureRandom3 = SecureRandom.getInstance("SHA1PRNG");
     */
    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * StandardPasswordEncoder 1024次迭代的SHA-256散列哈希加密实现，并使用一个随机8字节的salt。
     * 自定义密码加密器
     * 盐值不需要用户提供，每次随机生成；
     * public StandardPasswordEncoder(CharSequence secret) 可以设置一个秘钥值
     * 计算方式: 迭代SHA算法+密钥+随机盐来对密码加密，加密后得到的密码是80位
     */
    @Bean
    public static StandardPasswordEncoder standardPasswordEncoder() {
        return new StandardPasswordEncoder();
    }

    /**
     * 防止注解使用不了
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
