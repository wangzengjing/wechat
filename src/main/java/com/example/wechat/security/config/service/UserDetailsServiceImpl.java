package com.example.wechat.security.config.service;


import com.example.wechat.domain.SysUser;
import com.example.wechat.services.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser user = sysUserService.getUserByUsername(username);

        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList();

        if(user == null){
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return new User(user.getUsername(),user.getPassword(),grantedAuthorities);
    }
}
