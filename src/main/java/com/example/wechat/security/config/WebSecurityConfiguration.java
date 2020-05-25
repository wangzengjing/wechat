package com.example.wechat.security.config;

import com.example.wechat.security.config.service.UserDetailsServiceImpl;
import com.example.wechat.security.handler.AuthenctiationSuccessHandler;
import com.example.wechat.security.sessionstrategy.MyExpiredSessionStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true,jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private AuthenctiationSuccessHandler authenctiationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;



    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }


    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {



        http.authorizeRequests()
                //固定权限设置
                .antMatchers("/register","/validCode","/user/test").anonymous()//开放注册接口
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(authenctiationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                .and()
                //登录会话限制，限制一个用户账号
                .sessionManagement()
                .maximumSessions(1)
                .expiredSessionStrategy(new MyExpiredSessionStrategy());


        http.csrf().disable();


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//内存中储存账号密码
//        auth.inMemoryAuthentication().passwordEncoder(bCryptPasswordEncoder())
//                .withUser("wzj").password(bCryptPasswordEncoder().encode("123456")).roles("USER");

   //JDBC储存账号密码验证
        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring() .antMatchers(
                HttpMethod.GET,
                "/*.html",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/favicon.ico"
        );
    }
}
